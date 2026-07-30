package com.hes.server.observability;

import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Component
public class BulkheadGate {
    private final Map<String, Semaphore> gates = new ConcurrentHashMap<>();

    public <T> T execute(ResiliencePolicy policy, Supplier<T> action) {
        Semaphore sem = gates.computeIfAbsent(policy.id(), id -> new Semaphore(policy.bulkheadMaxConcurrent()));
        boolean acquired;
        try {
            acquired = sem.tryAcquire(policy.timeout().toMillis(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("bulkhead interrupted: " + policy.id());
        }
        if (!acquired) {
            throw new IllegalStateException("bulkhead saturated: " + policy.id());
        }
        try {
            return action.get();
        } finally {
            sem.release();
        }
    }
}