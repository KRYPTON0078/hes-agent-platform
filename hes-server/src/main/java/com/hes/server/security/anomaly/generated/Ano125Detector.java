package com.hes.server.security.anomaly.generated;

import com.hes.server.security.anomaly.AnomalyDetector;
import com.hes.server.security.anomaly.AnomalyFinding;
import com.hes.server.security.anomaly.AnomalySignal;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class Ano125Detector implements AnomalyDetector {
    @Override public String id() { return "ANO-125"; }
    @Override public String description() { return "Detect TELEMETRY_GAP when numeric value exceeds 15"; }
    @Override
    public Optional<AnomalyFinding> detect(AnomalySignal signal) {
        if (signal == null || signal.signalType() == null) { return Optional.empty(); }
        if ("TELEMETRY_GAP".equals(signal.signalType()) && signal.numericValue() > 15) {
            return Optional.of(new AnomalyFinding(id(), signal.deviceId(),
                "TELEMETRY_GAP exceeded threshold 15", signal.numericValue() / 15));
        }
        return Optional.empty();
    }
}