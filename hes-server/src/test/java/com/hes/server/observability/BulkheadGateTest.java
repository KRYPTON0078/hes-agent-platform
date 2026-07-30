package com.hes.server.observability;

import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.*;

class BulkheadGateTest {
    @Test
    void rejectsWhenSaturated() {
        BulkheadGate gate = new BulkheadGate();
        ResiliencePolicy policy = new ResiliencePolicy("t", Duration.ofMillis(50), 0, 1);
        AtomicInteger inside = new AtomicInteger();
        Thread holder = new Thread(() -> gate.execute(policy, () -> {
            inside.incrementAndGet();
            try { Thread.sleep(200); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            return null;
        }));
        holder.start();
        try { Thread.sleep(30); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        assertThrows(IllegalStateException.class, () -> gate.execute(policy, () -> "x"));
        try { holder.join(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        assertEquals(1, inside.get());
    }
}