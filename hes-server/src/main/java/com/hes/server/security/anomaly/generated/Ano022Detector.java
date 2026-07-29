package com.hes.server.security.anomaly.generated;

import com.hes.server.security.anomaly.AnomalyDetector;
import com.hes.server.security.anomaly.AnomalyFinding;
import com.hes.server.security.anomaly.AnomalySignal;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class Ano022Detector implements AnomalyDetector {
    @Override public String id() { return "ANO-022"; }
    @Override public String description() { return "Detect KEY_ROTATE_BURST when numeric value exceeds 32"; }
    @Override
    public Optional<AnomalyFinding> detect(AnomalySignal signal) {
        if (signal == null || signal.signalType() == null) { return Optional.empty(); }
        if ("KEY_ROTATE_BURST".equals(signal.signalType()) && signal.numericValue() > 32) {
            return Optional.of(new AnomalyFinding(id(), signal.deviceId(),
                "KEY_ROTATE_BURST exceeded threshold 32", signal.numericValue() / 32));
        }
        return Optional.empty();
    }
}