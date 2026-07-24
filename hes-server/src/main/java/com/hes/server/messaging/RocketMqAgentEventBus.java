package com.hes.server.messaging;

import com.hes.common.protocol.AgentMessage;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "hes.rocketmq", name = "enabled", havingValue = "true")
public class RocketMqAgentEventBus implements AgentEventBus {

    public static final String TELEMETRY_TOPIC = "hes-telemetry";
    public static final String COMMAND_TOPIC = "hes-command";

    private static final Logger log = LoggerFactory.getLogger(RocketMqAgentEventBus.class);

    private final RocketMQTemplate rocketMQTemplate;
    private final PendingCommandBuffer pendingCommandBuffer;

    public RocketMqAgentEventBus(RocketMQTemplate rocketMQTemplate, PendingCommandBuffer pendingCommandBuffer) {
        this.rocketMQTemplate = rocketMQTemplate;
        this.pendingCommandBuffer = pendingCommandBuffer;
    }

    @Override
    public void publishTelemetry(AgentMessage message) {
        log.debug("Publishing telemetry to RocketMQ device={}", message.deviceId());
        rocketMQTemplate.convertAndSend(TELEMETRY_TOPIC, message);
    }

    @Override
    public void publishCommand(AgentMessage message) {
        log.debug("Publishing command to RocketMQ device={}", message.deviceId());
        rocketMQTemplate.convertAndSend(COMMAND_TOPIC, message);
        // Fan-out to HTTP poll buffer is done by CommandRocketMqConsumer (and kept here as
        // a safety net if the consumer is briefly unavailable during broker warmup).
        pendingCommandBuffer.enqueue(message);
    }
}
