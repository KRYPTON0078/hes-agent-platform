package com.hes.server.security.anomaly.generated;

import com.hes.server.security.anomaly.AnomalyDetector;
import com.hes.server.security.anomaly.AnomalyFinding;
import com.hes.server.security.anomaly.AnomalySignal;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class Ano119Detector implements AnomalyDetector {
    @Override public String id() { return "ANO-119"; }
    @Override public String description() { return "Detect EXPORT_SPIKE when numeric value exceeds 49"; }
    @Override
    public Optional<AnomalyFinding> detect(AnomalySignal signal) {
        if (signal == null || signal.signalType() == null) { return Optional.empty(); }
        if ("EXPORT_SPIKE".equals(signal.signalType()) && signal.numericValue() > 49) {
            return Optional.of(new AnomalyFinding(id(), signal.deviceId(),
                "EXPORT_SPIKE exceeded threshold 49", signal.numericValue() / 49));
        }
        return Optional.empty();
    }
}