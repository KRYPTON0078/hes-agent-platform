package com.hes.server.observability;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Service
public class PathMetricsService {
    private final MeterRegistry registry;

    public PathMetricsService(MeterRegistry registry) {
        this.registry = registry;
    }

    public <T> T timed(CriticalPath path, Supplier<T> action) {
        Timer.Sample sample = Timer.start(registry);
        try {
            T result = action.get();
            registry.counter("hes.path.success", "path", path.name()).increment();
            return result;
        } catch (RuntimeException ex) {
            registry.counter("hes.path.error", "path", path.name()).increment();
            throw ex;
        } finally {
            sample.stop(Timer.builder("hes.path.latency").tag("path", path.name()).register(registry));
        }
    }

    public void recordLatency(CriticalPath path, long millis) {
        registry.timer("hes.path.latency", "path", path.name()).record(millis, TimeUnit.MILLISECONDS);
    }
}