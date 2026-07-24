package com.hes.server.messaging;

import com.hes.common.protocol.AgentMessage;

/**
 * Abstraction over RocketMQ (docker) and in-process bus (local).
 */
public interface AgentEventBus {
    void publishTelemetry(AgentMessage message);
    void publishCommand(AgentMessage message);
}
