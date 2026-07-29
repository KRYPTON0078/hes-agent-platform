package com.hes.server.security.anomaly.generated;

import com.hes.server.security.anomaly.AnomalyDetector;
import com.hes.server.security.anomaly.AnomalyFinding;
import com.hes.server.security.anomaly.AnomalySignal;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class Ano069Detector implements AnomalyDetector {
    @Override public String id() { return "ANO-069"; }
    @Override public String description() { return "Detect TELEMETRY_GAP when numeric value exceeds 39"; }
    @Override
    public Optional<AnomalyFinding> detect(AnomalySignal signal) {
        if (signal == null || signal.signalType() == null) { return Optional.empty(); }
        if ("TELEMETRY_GAP".equals(signal.signalType()) && signal.numericValue() > 39) {
            return Optional.of(new AnomalyFinding(id(), signal.deviceId(),
                "TELEMETRY_GAP exceeded threshold 39", signal.numericValue() / 39));
        }
        return Optional.empty();
    }
}