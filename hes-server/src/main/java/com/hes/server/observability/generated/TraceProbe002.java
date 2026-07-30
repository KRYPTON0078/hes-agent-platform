package com.hes.server.observability.generated;

import com.hes.server.observability.TraceProbe;
import org.springframework.stereotype.Component;

@Component
public class TraceProbe002 implements TraceProbe {
    @Override public String id() { return "TRC-002"; }
    @Override public String operation() { return "COMMAND_ACK"; }
    @Override public String spanName() { return "hes.command_ack.probe002"; }
    @Override public boolean shouldSample(double loadFactor) {
        return loadFactor >= 0.38;
    }
}