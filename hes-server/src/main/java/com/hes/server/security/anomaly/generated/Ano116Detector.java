package com.hes.server.security.anomaly.generated;

import com.hes.server.security.anomaly.AnomalyDetector;
import com.hes.server.security.anomaly.AnomalyFinding;
import com.hes.server.security.anomaly.AnomalySignal;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class Ano116Detector implements AnomalyDetector {
    @Override public String id() { return "ANO-116"; }
    @Override public String description() { return "Detect AUTH_FAIL_RATE when numeric value exceeds 46"; }
    @Override
    public Optional<AnomalyFinding> detect(AnomalySignal signal) {
        if (signal == null || signal.signalType() == null) { return Optional.empty(); }
        if ("AUTH_FAIL_RATE".equals(signal.signalType()) && signal.numericValue() > 46) {
            return Optional.of(new AnomalyFinding(id(), signal.deviceId(),
                "AUTH_FAIL_RATE exceeded threshold 46", signal.numericValue() / 46));
        }
        return Optional.empty();
    }
}