package com.hes.server.security.anomaly;

import java.util.Optional;

public interface AnomalyDetector {
    String id();
    String description();
    Optional<AnomalyFinding> detect(AnomalySignal signal);
}
