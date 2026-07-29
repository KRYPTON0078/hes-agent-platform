package com.hes.server.security.mtls;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InMemoryDeviceCertificateService implements DeviceCertificateService {

    private final Map<String, Boolean> revoked = new ConcurrentHashMap<>();

    @Override
    public String beginEnrollment(String deviceId, String csrPem) {
        if (deviceId == null || deviceId.isBlank() || csrPem == null || csrPem.isBlank()) {
            throw new IllegalArgumentException("deviceId and csrPem are required");
        }
        return "pending-enrollment:" + deviceId;
    }

    @Override
    public boolean isRevoked(String serialNumber) {
        return Boolean.TRUE.equals(revoked.get(serialNumber));
    }

    public void revoke(String serialNumber) {
        revoked.put(serialNumber, true);
    }
}
