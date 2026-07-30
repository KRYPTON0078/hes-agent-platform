package com.hes.server.ota.generated;

import com.hes.server.ota.OtaPhaseHandler;
import org.springframework.stereotype.Component;

@Component
public class OtaPhaseHandler044 implements OtaPhaseHandler {
    @Override public String id() { return "OTA-044"; }
    @Override public String fromPhase() { return "APPLYING"; }
    @Override public String toPhase() { return "ACKED"; }
    @Override public boolean canTransition(String currentPhase, boolean downloadOk, boolean applyOk, boolean agentAck) {
        if (!"APPLYING".equals(currentPhase)) return false;
        return downloadOk == true && applyOk == true && agentAck == true;
    }
}