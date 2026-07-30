package com.hes.server.observability.generated;

import com.hes.server.observability.TraceProbe;
import org.springframework.stereotype.Component;

@Component
public class TraceProbe011 implements TraceProbe {
    @Override public String id() { return "TRC-011"; }
    @Override public String operation() { return "DISPATCH_EVAL"; }
    @Override public String spanName() { return "hes.dispatch_eval.probe011"; }
    @Override public boolean shouldSample(double loadFactor) {
        return loadFactor >= 0.74;
    }
}