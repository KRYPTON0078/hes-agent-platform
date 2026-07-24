package com.hes.server.cache;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
@ConditionalOnBean(StringRedisTemplate.class)
@Primary
public class RedisCommandIdempotencyStore implements CommandIdempotencyStore {

    private static final String KEY_PREFIX = "hes:cmd:idem:";
    private static final Duration TTL = Duration.ofHours(48);

    private final StringRedisTemplate redis;

    public RedisCommandIdempotencyStore(StringRedisTemplate redis) {
        this.redis = redis;
    }

    @Override
    public boolean tryClaim(String idempotencyKey, String commandId) {
        Boolean ok = redis.opsForValue().setIfAbsent(KEY_PREFIX + idempotencyKey, commandId, TTL.toSeconds(), TimeUnit.SECONDS);
        return Boolean.TRUE.equals(ok);
    }
}
