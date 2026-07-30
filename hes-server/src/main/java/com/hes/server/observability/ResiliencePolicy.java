package com.hes.server.observability;

import java.time.Duration;

public record ResiliencePolicy(
        String id,
        Duration timeout,
        int maxRetries,
        int bulkheadMaxConcurrent
) {
    public static ResiliencePolicy agentIngest() {
        return new ResiliencePolicy("agent-ingest", Duration.ofMillis(800), 1, 64);
    }
    public static ResiliencePolicy commandPath() {
        return new ResiliencePolicy("command-path", Duration.ofMillis(700), 2, 32);
    }
    public static ResiliencePolicy dispatchPath() {
        return new ResiliencePolicy("dispatch-path", Duration.ofMillis(600), 1, 16);
    }
}