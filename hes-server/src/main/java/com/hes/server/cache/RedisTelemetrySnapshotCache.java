package com.hes.server.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hes.common.protocol.TelemetryPayload;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@Component
@ConditionalOnBean(StringRedisTemplate.class)
@Primary
public class RedisTelemetrySnapshotCache implements TelemetrySnapshotCache {

    private static final String KEY_PREFIX = "hes:telemetry:latest:";
    private static final Duration TTL = Duration.ofHours(24);

    private final StringRedisTemplate redis;
    private final ObjectMapper objectMapper;

    public RedisTelemetrySnapshotCache(StringRedisTemplate redis, ObjectMapper objectMapper) {
        this.redis = redis;
        this.objectMapper = objectMapper;
    }

    @Override
    public void put(String deviceId, TelemetryPayload payload) {
        try {
            redis.opsForValue().set(KEY_PREFIX + deviceId, objectMapper.writeValueAsString(payload), TTL);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to cache telemetry", e);
        }
    }

    @Override
    public Optional<TelemetryPayload> get(String deviceId) {
        String json = redis.opsForValue().get(KEY_PREFIX + deviceId);
        if (json == null || json.isBlank()) {
            return Optional.empty();
        }
        try {
            return Optional.of(objectMapper.readValue(json, TelemetryPayload.class));
        } catch (JsonProcessingException e) {
            return Optional.empty();
        }
    }
}
