package com.hes.server.security.anomaly;

import java.util.Map;

public record AnomalySignal(
        String deviceId,
        String signalType,
        double numericValue,
        Map<String, Object> tags
) {
}
