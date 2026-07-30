package com.hes.server.ota.generated;

import com.hes.server.ota.OtaPhaseHandler;
import org.springframework.stereotype.Component;

@Component
public class OtaPhaseHandler046 implements OtaPhaseHandler {
    @Override public String id() { return "OTA-046"; }
    @Override public String fromPhase() { return "APPLYING"; }
    @Override public String toPhase() { return "FAILED"; }
    @Override public boolean canTransition(String currentPhase, boolean downloadOk, boolean applyOk, boolean agentAck) {
        if (!"APPLYING".equals(currentPhase)) return false;
        return downloadOk == true && applyOk == false && agentAck == false;
    }
}