package com.hes.server.cache;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
@ConditionalOnMissingBean(StringRedisTemplate.class)
public class InMemoryCommandIdempotencyStore implements CommandIdempotencyStore {

    private final ConcurrentHashMap<String, String> keys = new ConcurrentHashMap<>();

    @Override
    public boolean tryClaim(String idempotencyKey, String commandId) {
        return keys.putIfAbsent(idempotencyKey, commandId) == null;
    }
}
