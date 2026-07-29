package com.hes.server.security.mtls;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryDeviceCertificateServiceTest {
    @Test
    void enrollAndRevoke() {
        InMemoryDeviceCertificateService svc = new InMemoryDeviceCertificateService();
        assertTrue(svc.beginEnrollment("D1", "CSR").startsWith("pending-enrollment:"));
        svc.revoke("SN-1");
        assertTrue(svc.isRevoked("SN-1"));
    }
}
