package com.hes.server.security.agentcred;

import com.hes.common.error.ErrorCode;
import com.hes.server.domain.device.DeviceCredentialEntity;
import com.hes.server.domain.device.DeviceCredentialRepository;
import com.hes.server.domain.device.DeviceEntity;
import com.hes.server.domain.device.DeviceRepository;
import com.hes.server.web.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HexFormat;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Agent API-key hashing (BCrypt preferred; legacy SHA-256 accepted during migration),
 * rotation, and failed-auth lockout.
 */
@Service
public class AgentCredentialService {

    private final DeviceRepository deviceRepository;
    private final DeviceCredentialRepository credentialRepository;
    private final PasswordEncoder passwordEncoder;
    private final int maxFailures;
    private final long lockSeconds;
    private final Map<String, AtomicInteger> failures = new ConcurrentHashMap<>();
    private final Map<String, Instant> lockedUntil = new ConcurrentHashMap<>();

    public AgentCredentialService(DeviceRepository deviceRepository,
                                  DeviceCredentialRepository credentialRepository,
                                  PasswordEncoder passwordEncoder,
                                  @Value("${hes.security.agent.max-failed-auths:8}") int maxFailures,
                                  @Value("${hes.security.agent.lock-seconds:300}") long lockSeconds) {
        this.deviceRepository = deviceRepository;
        this.credentialRepository = credentialRepository;
        this.passwordEncoder = passwordEncoder;
        this.maxFailures = maxFailures;
        this.lockSeconds = lockSeconds;
    }

    public String hashApiKey(String rawKey) {
        return "bcrypt:" + passwordEncoder.encode(rawKey);
    }

    public boolean matches(String rawKey, String storedHash) {
        if (storedHash == null || rawKey == null) {
            return false;
        }
        if (storedHash.startsWith("bcrypt:")) {
            return passwordEncoder.matches(rawKey, storedHash.substring("bcrypt:".length()));
        }
        // Legacy migration path
        return constantTimeEquals(storedHash, sha256(rawKey));
    }

    public void assertNotLocked(String deviceId) {
        Instant until = lockedUntil.get(deviceId);
        if (until != null && until.isAfter(Instant.now())) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "Device auth temporarily locked");
        }
    }

    public void recordFailure(String deviceId) {
        int count = failures.computeIfAbsent(deviceId, id -> new AtomicInteger()).incrementAndGet();
        if (count >= maxFailures) {
            lockedUntil.put(deviceId, Instant.now().plusSeconds(lockSeconds));
            failures.remove(deviceId);
        }
    }

    public void recordSuccess(String deviceId) {
        failures.remove(deviceId);
        lockedUntil.remove(deviceId);
    }

    @Transactional
    public Map<String, Object> rotate(String deviceId) {
        DeviceEntity device = deviceRepository.findByDeviceId(deviceId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DEVICE_NOT_FOUND, "Device not found"));
        DeviceCredentialEntity credential = credentialRepository.findByDevice_Id(device.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED, "No credential"));
        String newKey = UUID.randomUUID().toString().replace("-", "") + UUID.randomUUID().toString().replace("-", "");
        credential.setApiKeyHash(hashApiKey(newKey));
        credential.setActive(true);
        credentialRepository.save(credential);
        return Map.of("deviceId", deviceId, "apiKey", newKey, "rotatedAt", Instant.now().toString());
    }

    private static boolean constantTimeEquals(String a, String b) {
        if (a == null || b == null || a.length() != b.length()) {
            return false;
        }
        int result = 0;
        for (int i = 0; i < a.length(); i++) {
            result |= a.charAt(i) ^ b.charAt(i);
        }
        return result == 0;
    }

    static String sha256(String raw) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(raw.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
