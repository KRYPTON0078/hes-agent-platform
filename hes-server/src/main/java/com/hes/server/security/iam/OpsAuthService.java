package com.hes.server.security.iam;

import com.hes.common.error.ErrorCode;
import com.hes.server.security.JwtService;
import com.hes.server.web.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OpsAuthService {

    private final OpsUserRepository userRepository;
    private final OpsRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final int maxFailedLogins;
    private final long lockMinutes;
    private final long accessTtlSeconds;

    public OpsAuthService(OpsUserRepository userRepository,
                          OpsRoleRepository roleRepository,
                          PasswordEncoder passwordEncoder,
                          JwtService jwtService,
                          @Value("${hes.security.auth.max-failed-logins:5}") int maxFailedLogins,
                          @Value("${hes.security.auth.lock-minutes:15}") long lockMinutes,
                          @Value("${hes.security.jwt.access-ttl-seconds:900}") long accessTtlSeconds) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.maxFailedLogins = maxFailedLogins;
        this.lockMinutes = lockMinutes;
        this.accessTtlSeconds = accessTtlSeconds;
    }

    @Transactional
    public OpsUserEntity register(RegisterOpsUserRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new BusinessException(ErrorCode.VALIDATION_FAILED, "Username already exists");
        }
        String roleCode = request.roleCode() == null || request.roleCode().isBlank() ? "VIEWER" : request.roleCode();
        OpsRoleEntity role = roleRepository.findByRoleCode(roleCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.VALIDATION_FAILED, "Unknown role: " + roleCode));
        OpsUserEntity user = new OpsUserEntity();
        user.setUsername(request.username());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setRoles(Set.of(role));
        return userRepository.save(user);
    }

    @Transactional
    public TokenResponse login(LoginRequest request) {
        OpsUserEntity user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED, "Invalid credentials"));
        if (!user.isEnabled()) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "Account disabled");
        }
        if (user.getLockedUntil() != null && user.getLockedUntil().isAfter(Instant.now())) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "Account temporarily locked");
        }
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            user.setFailedLogins(user.getFailedLogins() + 1);
            if (user.getFailedLogins() >= maxFailedLogins) {
                user.setLockedUntil(Instant.now().plusSeconds(lockMinutes * 60));
                user.setFailedLogins(0);
            }
            userRepository.save(user);
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "Invalid credentials");
        }
        user.setFailedLogins(0);
        user.setLockedUntil(null);
        userRepository.save(user);
        var roles = user.getRoles().stream().map(OpsRoleEntity::getRoleCode).collect(Collectors.toList());
        return new TokenResponse(
                jwtService.createAccessToken(user.getUsername(), roles),
                jwtService.createRefreshToken(user.getUsername()),
                "Bearer",
                accessTtlSeconds,
                roles
        );
    }

    @Transactional(readOnly = true)
    public TokenResponse refresh(String refreshToken) {
        String username = jwtService.parseRefreshSubject(refreshToken);
        OpsUserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED, "Invalid refresh token"));
        if (!user.isEnabled()) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "Account disabled");
        }
        var roles = user.getRoles().stream().map(OpsRoleEntity::getRoleCode).collect(Collectors.toList());
        return new TokenResponse(
                jwtService.createAccessToken(user.getUsername(), roles),
                jwtService.createRefreshToken(user.getUsername()),
                "Bearer",
                accessTtlSeconds,
                roles
        );
    }
}
