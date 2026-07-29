package com.hes.server.security.anomaly.generated;

import com.hes.server.security.anomaly.AnomalyDetector;
import com.hes.server.security.anomaly.AnomalyFinding;
import com.hes.server.security.anomaly.AnomalySignal;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class Ano132Detector implements AnomalyDetector {
    @Override public String id() { return "ANO-132"; }
    @Override public String description() { return "Detect AUTH_FAIL_RATE when numeric value exceeds 22"; }
    @Override
    public Optional<AnomalyFinding> detect(AnomalySignal signal) {
        if (signal == null || signal.signalType() == null) { return Optional.empty(); }
        if ("AUTH_FAIL_RATE".equals(signal.signalType()) && signal.numericValue() > 22) {
            return Optional.of(new AnomalyFinding(id(), signal.deviceId(),
                "AUTH_FAIL_RATE exceeded threshold 22", signal.numericValue() / 22));
        }
        return Optional.empty();
    }
}