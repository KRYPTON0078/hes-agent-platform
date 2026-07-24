package com.hes.common.protocol;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProtocolVersionsTest {
    @Test
    void acceptsV1() {
        assertTrue(ProtocolVersions.isSupported("1.0"));
    }

    @Test
    void rejectsUnknown() {
        assertFalse(ProtocolVersions.isSupported("9.9"));
    }
}
