package com.hes.common.protocol;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class TelemetryPayloadTest {
    @Test
    void holdsEnergyDomainFields() {
        TelemetryPayload payload = new TelemetryPayload(
                BigDecimal.valueOf(55.5), BigDecimal.valueOf(5.5), BigDecimal.valueOf(1000),
                BigDecimal.valueOf(-1000), BigDecimal.valueOf(800), BigDecimal.valueOf(51.2),
                BigDecimal.valueOf(19.5), 0, null, "CHARGING");
        assertEquals(0, payload.faultCode());
        assertEquals("CHARGING", payload.operatingMode());
    }
}
