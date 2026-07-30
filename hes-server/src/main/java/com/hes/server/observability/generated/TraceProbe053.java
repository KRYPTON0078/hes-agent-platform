package com.hes.server.observability.generated;

import com.hes.server.observability.TraceProbe;
import org.springframework.stereotype.Component;

@Component
public class TraceProbe053 implements TraceProbe {
    @Override public String id() { return "TRC-053"; }
    @Override public String operation() { return "DISPATCH_EVAL"; }
    @Override public String spanName() { return "hes.dispatch_eval.probe053"; }
    @Override public boolean shouldSample(double loadFactor) {
        return loadFactor >= 0.62;
    }
}