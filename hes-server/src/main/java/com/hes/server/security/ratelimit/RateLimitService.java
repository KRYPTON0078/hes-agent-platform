package com.hes.server.security.ratelimit;

public interface RateLimitService {
    boolean allow(String key);
}
