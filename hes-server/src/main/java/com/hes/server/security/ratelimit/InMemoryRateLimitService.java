package com.hes.server.security.ratelimit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@ConditionalOnMissingBean(name = "redisRateLimitService")
public class InMemoryRateLimitService implements RateLimitService {

    private final int limit;
    private final Map<String, Window> windows = new ConcurrentHashMap<>();

    public InMemoryRateLimitService(@Value("${hes.security.rate-limit.per-minute:120}") int limit) {
        this.limit = limit;
    }

    @Override
    public boolean allow(String key) {
        return windows.computeIfAbsent(key, k -> new Window()).allow(limit);
    }

    private static final class Window {
        private long start = System.currentTimeMillis();
        private final AtomicInteger count = new AtomicInteger();

        synchronized boolean allow(int limit) {
            long now = System.currentTimeMillis();
            if (now - start >= 60_000) {
                start = now;
                count.set(0);
            }
            return count.incrementAndGet() <= limit;
        }
    }
}
