package com.hes.server.observability.generated;

import com.hes.server.observability.TraceProbe;
import org.springframework.stereotype.Component;

@Component
public class TraceProbe059 implements TraceProbe {
    @Override public String id() { return "TRC-059"; }
    @Override public String operation() { return "DISPATCH_EVAL"; }
    @Override public String spanName() { return "hes.dispatch_eval.probe059"; }
    @Override public boolean shouldSample(double loadFactor) {
        return loadFactor >= 0.86;
    }
}