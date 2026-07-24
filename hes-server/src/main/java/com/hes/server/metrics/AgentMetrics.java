package com.hes.server.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class AgentMetrics {
    private final Counter telemetryAccepted;
    private final Counter commandsIssued;
    private final Counter commandsTimedOut;

    public AgentMetrics(MeterRegistry registry) {
        this.telemetryAccepted = registry.counter("hes.telemetry.accepted");
        this.commandsIssued = registry.counter("hes.commands.issued");
        this.commandsTimedOut = registry.counter("hes.commands.timeout");
    }

    public void telemetryAccepted() { telemetryAccepted.increment(); }
    public void commandIssued() { commandsIssued.increment(); }
    public void commandTimedOut(int count) { commandsTimedOut.increment(count); }
}
