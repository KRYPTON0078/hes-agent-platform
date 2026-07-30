package com.hes.server.observability.generated;

import com.hes.server.observability.TraceProbe;
import org.springframework.stereotype.Component;

@Component
public class TraceProbe044 implements TraceProbe {
    @Override public String id() { return "TRC-044"; }
    @Override public String operation() { return "COMMAND_ACK"; }
    @Override public String spanName() { return "hes.command_ack.probe044"; }
    @Override public boolean shouldSample(double loadFactor) {
        return loadFactor >= 0.86;
    }
}