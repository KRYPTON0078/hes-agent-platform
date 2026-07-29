package com.hes.server.security.anomaly.generated;

import com.hes.server.security.anomaly.AnomalyDetector;
import com.hes.server.security.anomaly.AnomalyFinding;
import com.hes.server.security.anomaly.AnomalySignal;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class Ano073Detector implements AnomalyDetector {
    @Override public String id() { return "ANO-073"; }
    @Override public String description() { return "Detect CMD_FLOOD when numeric value exceeds 43"; }
    @Override
    public Optional<AnomalyFinding> detect(AnomalySignal signal) {
        if (signal == null || signal.signalType() == null) { return Optional.empty(); }
        if ("CMD_FLOOD".equals(signal.signalType()) && signal.numericValue() > 43) {
            return Optional.of(new AnomalyFinding(id(), signal.deviceId(),
                "CMD_FLOOD exceeded threshold 43", signal.numericValue() / 43));
        }
        return Optional.empty();
    }
}