package com.hes.server.observability.generated;

import com.hes.server.observability.TraceProbe;
import org.springframework.stereotype.Component;

@Component
public class TraceProbe010 implements TraceProbe {
    @Override public String id() { return "TRC-010"; }
    @Override public String operation() { return "SCHEDULE_EVAL"; }
    @Override public String spanName() { return "hes.schedule_eval.probe010"; }
    @Override public boolean shouldSample(double loadFactor) {
        return loadFactor >= 0.7;
    }
}