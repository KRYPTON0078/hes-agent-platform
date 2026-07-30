package com.hes.server.observability.generated;

import com.hes.server.observability.TraceProbe;
import org.springframework.stereotype.Component;

@Component
public class TraceProbe024 implements TraceProbe {
    @Override public String id() { return "TRC-024"; }
    @Override public String operation() { return "AGENT_REGISTER"; }
    @Override public String spanName() { return "hes.agent_register.probe024"; }
    @Override public boolean shouldSample(double loadFactor) {
        return loadFactor >= 0.66;
    }
}