package com.hes.server.observability;

public interface TraceProbe {
    String id();
    String operation();
    String spanName();
    boolean shouldSample(double loadFactor);
}