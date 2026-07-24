package com.hes.server.messaging;

import com.hes.common.protocol.AgentMessage;
import com.hes.server.service.TelemetryIngestService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "hes.rocketmq", name = "enabled", havingValue = "true")
@RocketMQMessageListener(topic = RocketMqAgentEventBus.TELEMETRY_TOPIC, consumerGroup = "hes-telemetry-consumer")
public class TelemetryRocketMqConsumer implements RocketMQListener<AgentMessage> {

    private static final Logger log = LoggerFactory.getLogger(TelemetryRocketMqConsumer.class);

    private final TelemetryIngestService telemetryIngestService;

    public TelemetryRocketMqConsumer(TelemetryIngestService telemetryIngestService) {
        this.telemetryIngestService = telemetryIngestService;
    }

    @Override
    public void onMessage(AgentMessage message) {
        log.debug("Consuming telemetry from RocketMQ device={}", message.deviceId());
        telemetryIngestService.persist(message);
    }
}
