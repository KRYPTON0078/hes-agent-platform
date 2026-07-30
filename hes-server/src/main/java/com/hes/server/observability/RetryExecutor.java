package com.hes.server.observability;

import org.springframework.stereotype.Component;
import java.util.function.Supplier;

@Component
public class RetryExecutor {
    public <T> T execute(ResiliencePolicy policy, Supplier<T> action) {
        RuntimeException last = null;
        int attempts = Math.max(1, policy.maxRetries() + 1);
        for (int i = 0; i < attempts; i++) {
            try {
                return action.get();
            } catch (RuntimeException ex) {
                last = ex;
            }
        }
        throw last == null ? new IllegalStateException("retry failed") : last;
    }
}