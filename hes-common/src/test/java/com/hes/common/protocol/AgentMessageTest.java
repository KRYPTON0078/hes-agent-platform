package com.hes.common.protocol;

import org.junit.jupiter.api.Test;
import java.util.Map;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class AgentMessageTest {

    @Test
    void factorySetsProtocolVersionAndTimestamp() {
        AgentMessage message = AgentMessage.of(
                MessageType.HEARTBEAT,
                UUID.randomUUID().toString(),
                "DEV-1",
                Map.of("ok", true)
        );
        assertEquals(AgentMessage.PROTOCOL_V1, message.protocolVersion());
        assertEquals(MessageType.HEARTBEAT, message.type());
        assertNotNull(message.timestamp());
    }
}
