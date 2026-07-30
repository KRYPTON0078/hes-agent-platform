package com.hes.server.ota.generated;

import com.hes.server.ota.OtaPhaseHandler;
import org.springframework.stereotype.Component;

@Component
public class OtaPhaseHandler069 implements OtaPhaseHandler {
    @Override public String id() { return "OTA-069"; }
    @Override public String fromPhase() { return "ACKED"; }
    @Override public String toPhase() { return "CREATED"; }
    @Override public boolean canTransition(String currentPhase, boolean downloadOk, boolean applyOk, boolean agentAck) {
        if (!"ACKED".equals(currentPhase)) return false;
        return downloadOk == false && applyOk == false && agentAck == false;
    }
}