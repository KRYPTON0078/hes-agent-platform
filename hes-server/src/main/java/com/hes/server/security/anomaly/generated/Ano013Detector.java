package com.hes.server.security.anomaly.generated;

import com.hes.server.security.anomaly.AnomalyDetector;
import com.hes.server.security.anomaly.AnomalyFinding;
import com.hes.server.security.anomaly.AnomalySignal;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class Ano013Detector implements AnomalyDetector {
    @Override public String id() { return "ANO-013"; }
    @Override public String description() { return "Detect TELEMETRY_GAP when numeric value exceeds 23"; }
    @Override
    public Optional<AnomalyFinding> detect(AnomalySignal signal) {
        if (signal == null || signal.signalType() == null) { return Optional.empty(); }
        if ("TELEMETRY_GAP".equals(signal.signalType()) && signal.numericValue() > 23) {
            return Optional.of(new AnomalyFinding(id(), signal.deviceId(),
                "TELEMETRY_GAP exceeded threshold 23", signal.numericValue() / 23));
        }
        return Optional.empty();
    }
}