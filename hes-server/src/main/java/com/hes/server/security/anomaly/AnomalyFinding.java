package com.hes.server.security.anomaly;

public record AnomalyFinding(
        String detectorId,
        String deviceId,
        String summary,
        double score
) {
}
