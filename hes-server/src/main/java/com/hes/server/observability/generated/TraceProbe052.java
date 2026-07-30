package com.hes.server.observability.generated;

import com.hes.server.observability.TraceProbe;
import org.springframework.stereotype.Component;

@Component
public class TraceProbe052 implements TraceProbe {
    @Override public String id() { return "TRC-052"; }
    @Override public String operation() { return "SCHEDULE_EVAL"; }
    @Override public String spanName() { return "hes.schedule_eval.probe052"; }
    @Override public boolean shouldSample(double loadFactor) {
        return loadFactor >= 0.58;
    }
}