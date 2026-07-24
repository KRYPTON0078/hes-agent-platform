package com.hes.server.presence;

import com.hes.server.config.HesProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@ConditionalOnMissingBean(name = "redisOnlinePresenceStore")
public class InMemoryOnlinePresenceStore implements OnlinePresenceStore {

    private final Map<String, Instant> lastSeen = new ConcurrentHashMap<>();
    private final Duration ttl;

    public InMemoryOnlinePresenceStore(HesProperties properties) {
        this.ttl = Duration.ofSeconds(properties.getAgent().getHeartbeatTtlSeconds());
    }

    @Override
    public void heartbeat(String deviceId, Instant seenAt) {
        lastSeen.put(deviceId, seenAt);
    }

    @Override
    public boolean isOnline(String deviceId) {
        return lastSeen(deviceId).isPresent();
    }

    @Override
    public Optional<Instant> lastSeen(String deviceId) {
        Instant seen = lastSeen.get(deviceId);
        if (seen == null) {
            return Optional.empty();
        }
        if (seen.plus(ttl).isBefore(Instant.now())) {
            lastSeen.remove(deviceId, seen);
            return Optional.empty();
        }
        return Optional.of(seen);
    }

    @Override
    public Set<String> onlineDeviceIds() {
        Instant cutoff = Instant.now().minus(ttl);
        return lastSeen.entrySet().stream()
                .filter(e -> !e.getValue().isBefore(cutoff))
                .map(Map.Entry::getKey)
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public long onlineCount() {
        return onlineDeviceIds().size();
    }
}
