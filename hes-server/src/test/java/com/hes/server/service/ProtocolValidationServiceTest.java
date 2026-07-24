package com.hes.server.service;

import com.hes.common.protocol.AgentMessage;
import com.hes.common.protocol.MessageType;
import com.hes.server.web.BusinessException;
import org.junit.jupiter.api.Test;
import java.util.Map;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class ProtocolValidationServiceTest {
    private final ProtocolValidationService service = new ProtocolValidationService();

    @Test
    void acceptsValidEnvelope() {
        AgentMessage message = AgentMessage.of(MessageType.HEARTBEAT, UUID.randomUUID().toString(), "D1", Map.of());
        assertDoesNotThrow(() -> service.validateEnvelope(message));
    }

    @Test
    void rejectsMissingDeviceId() {
        AgentMessage message = new AgentMessage("1.0", MessageType.HEARTBEAT, "m1", " ", null, Map.of());
        assertThrows(BusinessException.class, () -> service.validateEnvelope(message));
    }
}
