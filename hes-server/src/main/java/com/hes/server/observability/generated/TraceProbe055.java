package com.hes.server.observability.generated;

import com.hes.server.observability.TraceProbe;
import org.springframework.stereotype.Component;

@Component
public class TraceProbe055 implements TraceProbe {
    @Override public String id() { return "TRC-055"; }
    @Override public String operation() { return "AGENT_TELEMETRY"; }
    @Override public String spanName() { return "hes.agent_telemetry.probe055"; }
    @Override public boolean shouldSample(double loadFactor) {
        return loadFactor >= 0.7;
    }
}