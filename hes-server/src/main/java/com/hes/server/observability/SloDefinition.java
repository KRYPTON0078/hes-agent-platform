package com.hes.server.observability;

public record SloDefinition(
        String id,
        CriticalPath path,
        double availabilityTarget,
        long latencyP99Ms
) {}