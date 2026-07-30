package com.hes.server.ota.generated;

import com.hes.server.ota.OtaPhaseHandler;
import org.springframework.stereotype.Component;

@Component
public class OtaPhaseHandler021 implements OtaPhaseHandler {
    @Override public String id() { return "OTA-021"; }
    @Override public String fromPhase() { return "CREATED"; }
    @Override public String toPhase() { return "DOWNLOADING"; }
    @Override public boolean canTransition(String currentPhase, boolean downloadOk, boolean applyOk, boolean agentAck) {
        if (!"CREATED".equals(currentPhase)) return false;
        return downloadOk == true && applyOk == false && agentAck == false;
    }
}