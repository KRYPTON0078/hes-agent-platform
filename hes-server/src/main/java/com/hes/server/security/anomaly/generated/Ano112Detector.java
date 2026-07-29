package com.hes.server.security.anomaly.generated;

import com.hes.server.security.anomaly.AnomalyDetector;
import com.hes.server.security.anomaly.AnomalyFinding;
import com.hes.server.security.anomaly.AnomalySignal;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class Ano112Detector implements AnomalyDetector {
    @Override public String id() { return "ANO-112"; }
    @Override public String description() { return "Detect SOC_DROP when numeric value exceeds 42"; }
    @Override
    public Optional<AnomalyFinding> detect(AnomalySignal signal) {
        if (signal == null || signal.signalType() == null) { return Optional.empty(); }
        if ("SOC_DROP".equals(signal.signalType()) && signal.numericValue() > 42) {
            return Optional.of(new AnomalyFinding(id(), signal.deviceId(),
                "SOC_DROP exceeded threshold 42", signal.numericValue() / 42));
        }
        return Optional.empty();
    }
}