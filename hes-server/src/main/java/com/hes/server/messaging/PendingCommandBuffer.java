package com.hes.server.messaging;

import com.hes.common.protocol.AgentMessage;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Holds outbound commands for Agents to poll (HTTP demo path).
 */
@Component
public class PendingCommandBuffer {

    private final Map<String, List<AgentMessage>> byDevice = new ConcurrentHashMap<>();

    public void enqueue(AgentMessage message) {
        byDevice.compute(message.deviceId(), (id, list) -> {
            List<AgentMessage> next = list == null ? new ArrayList<>() : new ArrayList<>(list);
            next.add(message);
            return next;
        });
    }

    public List<AgentMessage> drain(String deviceId) {
        List<AgentMessage> messages = byDevice.remove(deviceId);
        return messages == null ? List.of() : List.copyOf(messages);
    }
}
