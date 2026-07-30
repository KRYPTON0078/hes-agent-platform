package com.hes.server.observability.generated;

import com.hes.server.observability.TraceProbe;
import org.springframework.stereotype.Component;

@Component
public class TraceProbe009 implements TraceProbe {
    @Override public String id() { return "TRC-009"; }
    @Override public String operation() { return "OPS_AUTH"; }
    @Override public String spanName() { return "hes.ops_auth.probe009"; }
    @Override public boolean shouldSample(double loadFactor) {
        return loadFactor >= 0.66;
    }
}