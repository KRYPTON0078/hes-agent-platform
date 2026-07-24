package com.hes.server.messaging;

import com.hes.common.protocol.AgentMessage;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Consumes command dispatch events from RocketMQ and feeds the Agent poll buffer.
 * HTTP Agents still receive commands via GET /api/v1/agent/{deviceId}/commands.
 */
@Component
@ConditionalOnProperty(prefix = "hes.rocketmq", name = "enabled", havingValue = "true")
@RocketMQMessageListener(topic = RocketMqAgentEventBus.COMMAND_TOPIC, consumerGroup = "hes-command-consumer")
public class CommandRocketMqConsumer implements RocketMQListener<AgentMessage> {

    private static final Logger log = LoggerFactory.getLogger(CommandRocketMqConsumer.class);

    private final PendingCommandBuffer pendingCommandBuffer;

    public CommandRocketMqConsumer(PendingCommandBuffer pendingCommandBuffer) {
        this.pendingCommandBuffer = pendingCommandBuffer;
    }

    @Override
    public void onMessage(AgentMessage message) {
        log.debug("Consuming command from RocketMQ device={} messageId={}", message.deviceId(), message.messageId());
        pendingCommandBuffer.enqueue(message);
    }
}
