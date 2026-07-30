package com.hes.server.ota.generated;

import com.hes.server.ota.OtaPhaseHandler;
import org.springframework.stereotype.Component;

@Component
public class OtaPhaseHandler059 implements OtaPhaseHandler {
    @Override public String id() { return "OTA-059"; }
    @Override public String fromPhase() { return "DOWNLOADING"; }
    @Override public String toPhase() { return "FAILED"; }
    @Override public boolean canTransition(String currentPhase, boolean downloadOk, boolean applyOk, boolean agentAck) {
        if (!"DOWNLOADING".equals(currentPhase)) return false;
        return downloadOk == false && applyOk == false && agentAck == false;
    }
}