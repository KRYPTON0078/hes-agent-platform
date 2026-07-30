package com.hes.server.observability.generated;

import com.hes.server.observability.TraceProbe;
import org.springframework.stereotype.Component;

@Component
public class TraceProbe041 implements TraceProbe {
    @Override public String id() { return "TRC-041"; }
    @Override public String operation() { return "DISPATCH_EVAL"; }
    @Override public String spanName() { return "hes.dispatch_eval.probe041"; }
    @Override public boolean shouldSample(double loadFactor) {
        return loadFactor >= 0.74;
    }
}