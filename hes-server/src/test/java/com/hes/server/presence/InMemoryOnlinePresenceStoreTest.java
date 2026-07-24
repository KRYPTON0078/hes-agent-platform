package com.hes.server.presence;

import com.hes.server.config.HesProperties;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryOnlinePresenceStoreTest {
    @Test
    void heartbeatMarksDeviceOnline() {
        HesProperties props = new HesProperties();
        InMemoryOnlinePresenceStore store = new InMemoryOnlinePresenceStore(props);
        store.heartbeat("D1", Instant.now());
        assertTrue(store.isOnline("D1"));
        assertEquals(1, store.onlineCount());
    }
}
