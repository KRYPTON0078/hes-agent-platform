package com.hes.server.security.anomaly.generated;

import com.hes.server.security.anomaly.AnomalyDetector;
import com.hes.server.security.anomaly.AnomalyFinding;
import com.hes.server.security.anomaly.AnomalySignal;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class Ano023Detector implements AnomalyDetector {
    @Override public String id() { return "ANO-023"; }
    @Override public String description() { return "Detect EXPORT_SPIKE when numeric value exceeds 33"; }
    @Override
    public Optional<AnomalyFinding> detect(AnomalySignal signal) {
        if (signal == null || signal.signalType() == null) { return Optional.empty(); }
        if ("EXPORT_SPIKE".equals(signal.signalType()) && signal.numericValue() > 33) {
            return Optional.of(new AnomalyFinding(id(), signal.deviceId(),
                "EXPORT_SPIKE exceeded threshold 33", signal.numericValue() / 33));
        }
        return Optional.empty();
    }
}