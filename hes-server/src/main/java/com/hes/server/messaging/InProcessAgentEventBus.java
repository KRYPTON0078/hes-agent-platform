package com.hes.server.messaging;

import com.hes.common.protocol.AgentMessage;
import com.hes.server.service.TelemetryIngestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * Local-dev bus: processes messages in-process when RocketMQ is disabled.
 */
@Component
@ConditionalOnProperty(prefix = "hes.rocketmq", name = "enabled", havingValue = "false", matchIfMissing = true)
public class InProcessAgentEventBus implements AgentEventBus {

    private static final Logger log = LoggerFactory.getLogger(InProcessAgentEventBus.class);

    private final TelemetryIngestService telemetryIngestService;
    private final PendingCommandBuffer pendingCommandBuffer;

    public InProcessAgentEventBus(@Lazy TelemetryIngestService telemetryIngestService,
                                  PendingCommandBuffer pendingCommandBuffer) {
        this.telemetryIngestService = telemetryIngestService;
        this.pendingCommandBuffer = pendingCommandBuffer;
    }

    @Override
    public void publishTelemetry(AgentMessage message) {
        log.debug("In-process telemetry for device={}", message.deviceId());
        telemetryIngestService.persist(message);
    }

    @Override
    public void publishCommand(AgentMessage message) {
        log.debug("In-process command dispatch device={} messageId={}", message.deviceId(), message.messageId());
        pendingCommandBuffer.enqueue(message);
    }
}
