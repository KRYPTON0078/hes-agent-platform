package com.hes.server.protocol.v11;

import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Enhanced HTTP long-poll duplex channel for Agent commands (WebSocket-ready queueing).
 */
@Service
public class DuplexCommandPollService {
    private final Map<String, LinkedBlockingQueue<Map<String, Object>>> queues = new ConcurrentHashMap<>();

    public void enqueue(String deviceId, Map<String, Object> command) {
        queues.computeIfAbsent(deviceId, id -> new LinkedBlockingQueue<>()).offer(command);
    }

    public Optional<Map<String, Object>> poll(String deviceId, Duration wait) throws InterruptedException {
        LinkedBlockingQueue<Map<String, Object>> q = queues.computeIfAbsent(deviceId, id -> new LinkedBlockingQueue<>());
        Map<String, Object> cmd = q.poll(wait.toMillis(), TimeUnit.MILLISECONDS);
        return Optional.ofNullable(cmd);
    }
}