package com.hes.server.security.enrollment;

import com.hes.common.error.ErrorCode;
import com.hes.server.web.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.HexFormat;
import java.util.UUID;

@Service
public class EnrollmentTokenService {
    private final EnrollmentTokenRepository repository;

    public EnrollmentTokenService(EnrollmentTokenRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public String issue(String siteCode, Instant expiresAt) {
        String raw = UUID.randomUUID().toString().replace("-", "") + UUID.randomUUID().toString().replace("-", "");
        EnrollmentTokenEntity e = new EnrollmentTokenEntity();
        e.setTokenHash(sha256(raw));
        e.setSiteCode(siteCode);
        e.setExpiresAt(expiresAt);
        repository.save(e);
        return raw;
    }

    @Transactional
    public String consume(String rawToken, String deviceId) {
        EnrollmentTokenEntity e = repository.findByTokenHash(sha256(rawToken))
                .orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED, "invalid enrollment token"));
        if (e.getConsumedAt() != null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "enrollment token already used");
        }
        if (Instant.now().isAfter(e.getExpiresAt())) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "enrollment token expired");
        }
        e.setConsumedAt(Instant.now());
        e.setConsumedByDevice(deviceId);
        repository.save(e);
        return e.getSiteCode();
    }

    static String sha256(String raw) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(md.digest(raw.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }
}