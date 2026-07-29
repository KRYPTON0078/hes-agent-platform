package com.hes.server.security.ratelimit;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryRateLimitServiceTest {
    @Test
    void enforcesPerMinuteBudget() {
        InMemoryRateLimitService svc = new InMemoryRateLimitService(2);
        assertTrue(svc.allow("a"));
        assertTrue(svc.allow("a"));
        assertFalse(svc.allow("a"));
    }
}
