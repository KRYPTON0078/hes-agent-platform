package com.hes.server.security.anomaly.generated;

import com.hes.server.security.anomaly.AnomalyDetector;
import com.hes.server.security.anomaly.AnomalyFinding;
import com.hes.server.security.anomaly.AnomalySignal;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class Ano115Detector implements AnomalyDetector {
    @Override public String id() { return "ANO-115"; }
    @Override public String description() { return "Detect VOLTAGE_SPIKE when numeric value exceeds 45"; }
    @Override
    public Optional<AnomalyFinding> detect(AnomalySignal signal) {
        if (signal == null || signal.signalType() == null) { return Optional.empty(); }
        if ("VOLTAGE_SPIKE".equals(signal.signalType()) && signal.numericValue() > 45) {
            return Optional.of(new AnomalyFinding(id(), signal.deviceId(),
                "VOLTAGE_SPIKE exceeded threshold 45", signal.numericValue() / 45));
        }
        return Optional.empty();
    }
}