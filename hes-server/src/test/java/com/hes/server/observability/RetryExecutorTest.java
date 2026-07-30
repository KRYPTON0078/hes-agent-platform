package com.hes.server.observability;

import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.*;

class RetryExecutorTest {
    @Test
    void retriesUntilSuccess() {
        RetryExecutor exec = new RetryExecutor();
        AtomicInteger n = new AtomicInteger();
        ResiliencePolicy policy = new ResiliencePolicy("r", Duration.ofMillis(100), 2, 1);
        String out = exec.execute(policy, () -> {
            if (n.incrementAndGet() < 3) throw new IllegalStateException("fail");
            return "ok";
        });
        assertEquals("ok", out);
        assertEquals(3, n.get());
    }
}