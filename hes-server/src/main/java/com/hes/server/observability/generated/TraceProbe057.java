package com.hes.server.observability.generated;

import com.hes.server.observability.TraceProbe;
import org.springframework.stereotype.Component;

@Component
public class TraceProbe057 implements TraceProbe {
    @Override public String id() { return "TRC-057"; }
    @Override public String operation() { return "OPS_AUTH"; }
    @Override public String spanName() { return "hes.ops_auth.probe057"; }
    @Override public boolean shouldSample(double loadFactor) {
        return loadFactor >= 0.78;
    }
}