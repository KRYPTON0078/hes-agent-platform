package com.hes.server.observability.generated;

import com.hes.server.observability.TraceProbe;
import org.springframework.stereotype.Component;

@Component
public class TraceProbe039 implements TraceProbe {
    @Override public String id() { return "TRC-039"; }
    @Override public String operation() { return "OPS_AUTH"; }
    @Override public String spanName() { return "hes.ops_auth.probe039"; }
    @Override public boolean shouldSample(double loadFactor) {
        return loadFactor >= 0.66;
    }
}