package com.hes.server.security.anomaly;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class AnomalyDetectionService {
    private final List<AnomalyDetector> detectors;

    public AnomalyDetectionService(List<AnomalyDetector> detectors) {
        this.detectors = List.copyOf(detectors);
    }

    public List<AnomalyFinding> evaluate(AnomalySignal signal) {
        List<AnomalyFinding> findings = new ArrayList<>();
        for (AnomalyDetector detector : detectors) {
            detector.detect(signal).ifPresent(findings::add);
        }
        return findings;
    }
}
