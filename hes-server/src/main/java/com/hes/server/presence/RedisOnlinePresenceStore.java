package com.hes.server.presence;

import com.hes.server.config.HesProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component("redisOnlinePresenceStore")
@ConditionalOnBean(StringRedisTemplate.class)
@Primary
public class RedisOnlinePresenceStore implements OnlinePresenceStore {

    private static final String KEY_PREFIX = "hes:agent:online:device:";
    private static final String INDEX_KEY = "hes:agent:online:zset";

    private final StringRedisTemplate redis;
    private final Duration ttl;

    public RedisOnlinePresenceStore(StringRedisTemplate redis, HesProperties properties) {
        this.redis = redis;
        this.ttl = Duration.ofSeconds(properties.getAgent().getHeartbeatTtlSeconds());
    }

    @Override
    public void heartbeat(String deviceId, Instant seenAt) {
        String key = KEY_PREFIX + deviceId;
        redis.opsForValue().set(key, seenAt.toString(), ttl);
        redis.opsForZSet().add(INDEX_KEY, deviceId, seenAt.toEpochMilli());
        redis.expire(INDEX_KEY, ttl.toSeconds() * 2, TimeUnit.SECONDS);
    }

    @Override
    public boolean isOnline(String deviceId) {
        return Boolean.TRUE.equals(redis.hasKey(KEY_PREFIX + deviceId));
    }

    @Override
    public Optional<Instant> lastSeen(String deviceId) {
        String value = redis.opsForValue().get(KEY_PREFIX + deviceId);
        if (value == null || value.isBlank()) {
            return Optional.empty();
        }
        return Optional.of(Instant.parse(value));
    }

    @Override
    public Set<String> onlineDeviceIds() {
        long cutoff = Instant.now().minus(ttl).toEpochMilli();
        Set<String> members = redis.opsForZSet().rangeByScore(INDEX_KEY, cutoff, Double.POSITIVE_INFINITY);
        if (members == null || members.isEmpty()) {
            return Collections.emptySet();
        }
        return members.stream()
                .filter(this::isOnline)
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public long onlineCount() {
        return onlineDeviceIds().size();
    }
}
