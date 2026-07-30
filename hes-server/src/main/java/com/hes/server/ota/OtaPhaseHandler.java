package com.hes.server.ota;

public interface OtaPhaseHandler {
    String id();
    String fromPhase();
    String toPhase();
    boolean canTransition(String currentPhase, boolean downloadOk, boolean applyOk, boolean agentAck);
}