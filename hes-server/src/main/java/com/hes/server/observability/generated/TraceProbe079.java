package com.hes.server.observability.generated;

import com.hes.server.observability.TraceProbe;
import org.springframework.stereotype.Component;

@Component
public class TraceProbe079 implements TraceProbe {
    @Override public String id() { return "TRC-079"; }
    @Override public String operation() { return "AGENT_TELEMETRY"; }
    @Override public String spanName() { return "hes.agent_telemetry.probe079"; }
    @Override public boolean shouldSample(double loadFactor) {
        return loadFactor >= 0.46;
    }
}