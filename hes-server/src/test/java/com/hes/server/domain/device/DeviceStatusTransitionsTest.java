package com.hes.server.domain.device;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DeviceStatusTransitionsTest {
    @Test
    void allowsOnlineToOffline() {
        assertTrue(DeviceStatusTransitions.canTransition(DeviceStatus.ONLINE, DeviceStatus.OFFLINE));
    }

    @Test
    void rejectsDisabledToOnlineDirectly() {
        assertFalse(DeviceStatusTransitions.canTransition(DeviceStatus.DISABLED, DeviceStatus.ONLINE));
    }
}
