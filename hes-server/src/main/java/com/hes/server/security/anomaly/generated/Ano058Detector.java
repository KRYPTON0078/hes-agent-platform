package com.hes.server.security.anomaly.generated;

import com.hes.server.security.anomaly.AnomalyDetector;
import com.hes.server.security.anomaly.AnomalyFinding;
import com.hes.server.security.anomaly.AnomalySignal;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class Ano058Detector implements AnomalyDetector {
    @Override public String id() { return "ANO-058"; }
    @Override public String description() { return "Detect OFFLINE_BURST when numeric value exceeds 28"; }
    @Override
    public Optional<AnomalyFinding> detect(AnomalySignal signal) {
        if (signal == null || signal.signalType() == null) { return Optional.empty(); }
        if ("OFFLINE_BURST".equals(signal.signalType()) && signal.numericValue() > 28) {
            return Optional.of(new AnomalyFinding(id(), signal.deviceId(),
                "OFFLINE_BURST exceeded threshold 28", signal.numericValue() / 28));
        }
        return Optional.empty();
    }
}