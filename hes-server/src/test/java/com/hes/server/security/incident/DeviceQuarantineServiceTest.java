package com.hes.server.security.incident;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DeviceQuarantineServiceTest {
    @Test
    void quarantineAndRelease() {
        DeviceQuarantineService svc = new DeviceQuarantineService();
        svc.quarantine("D1", "cmd-flood");
        assertTrue(svc.isQuarantined("D1"));
        svc.release("D1");
        assertFalse(svc.isQuarantined("D1"));
    }
}
