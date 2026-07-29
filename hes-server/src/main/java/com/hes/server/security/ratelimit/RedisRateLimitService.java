package com.hes.server.security.ratelimit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component("redisRateLimitService")
@ConditionalOnBean(StringRedisTemplate.class)
@Primary
public class RedisRateLimitService implements RateLimitService {

    private final StringRedisTemplate redis;
    private final int limit;

    public RedisRateLimitService(StringRedisTemplate redis,
                                 @Value("${hes.security.rate-limit.per-minute:120}") int limit) {
        this.redis = redis;
        this.limit = limit;
    }

    @Override
    public boolean allow(String key) {
        String redisKey = "hes:ratelimit:" + key;
        Long count = redis.opsForValue().increment(redisKey);
        if (count != null && count == 1L) {
            redis.expire(redisKey, Duration.ofMinutes(1));
        }
        return count != null && count <= limit;
    }
}
