package com.hes.server.cache;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@ConditionalOnMissingBean(StringRedisTemplate.class)
public class NoOpTelemetrySnapshotCache implements TelemetrySnapshotCache {

    @Override
    public void put(String deviceId, com.hes.common.protocol.TelemetryPayload payload) {
        // local profile without Redis
    }

    @Override
    public Optional<com.hes.common.protocol.TelemetryPayload> get(String deviceId) {
        return Optional.empty();
    }
}
