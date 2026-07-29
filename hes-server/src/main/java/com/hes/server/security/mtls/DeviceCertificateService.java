package com.hes.server.security.mtls;

/**
 * Placeholder for future device certificate enrollment (CSR → sign → install).
 * Wave N delivers full mTLS; this SPI keeps the boundary explicit.
 */
public interface DeviceCertificateService {
    String beginEnrollment(String deviceId, String csrPem);
    boolean isRevoked(String serialNumber);
}
