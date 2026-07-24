package com.hes.common.protocol;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.Map;

/**
 * Envelope for all Agent ↔ backend messages (protocol v1).
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AgentMessage(
        String protocolVersion,
        @NotNull MessageType type,
        @NotBlank String messageId,
        @NotBlank String deviceId,
        Instant timestamp,
        Map<String, Object> payload
) {
    public static final String PROTOCOL_V1 = "1.0";

    public static AgentMessage of(MessageType type, String messageId, String deviceId, Map<String, Object> payload) {
        return new AgentMessage(PROTOCOL_V1, type, messageId, deviceId, Instant.now(), payload);
    }
}
