package com.hes.server.energy.dispatch;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class DispatchMqBridge {
    private static final Logger log = LoggerFactory.getLogger(DispatchMqBridge.class);
    private final DispatchEventRepository eventRepository;
    private final RocketMQTemplate rocketMQTemplate;
    private final String topic;

    public DispatchMqBridge(DispatchEventRepository eventRepository,
                            RocketMQTemplate rocketMQTemplate,
                            @Value("") String topic) {
        this.eventRepository = eventRepository;
        this.rocketMQTemplate = rocketMQTemplate;
        this.topic = topic;
    }

    @Transactional
    public int publishPending() {
        List<DispatchEventEntity> pending = eventRepository.findByPublishedFalseOrderByCreatedAtAsc();
        int n = 0;
        for (DispatchEventEntity event : pending) {
            try {
                rocketMQTemplate.convertAndSend(topic, event.getPayloadJson());
                event.setPublished(true);
                eventRepository.save(event);
                n++;
            } catch (Exception ex) {
                log.warn("Failed to publish dispatch event {}: {}", event.getEventId(), ex.getMessage());
            }
        }
        return n;
    }
}