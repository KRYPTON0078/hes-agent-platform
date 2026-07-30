package com.hes.server.observability.generated;

import com.hes.server.observability.TraceProbe;
import org.springframework.stereotype.Component;

@Component
public class TraceProbe032 implements TraceProbe {
    @Override public String id() { return "TRC-032"; }
    @Override public String operation() { return "COMMAND_ACK"; }
    @Override public String spanName() { return "hes.command_ack.probe032"; }
    @Override public boolean shouldSample(double loadFactor) {
        return loadFactor >= 0.38;
    }
}