package com.hes.server.observability.generated;

import com.hes.server.observability.TraceProbe;
import org.springframework.stereotype.Component;

@Component
public class TraceProbe061 implements TraceProbe {
    @Override public String id() { return "TRC-061"; }
    @Override public String operation() { return "AGENT_TELEMETRY"; }
    @Override public String spanName() { return "hes.agent_telemetry.probe061"; }
    @Override public boolean shouldSample(double loadFactor) {
        return loadFactor >= 0.34;
    }
}