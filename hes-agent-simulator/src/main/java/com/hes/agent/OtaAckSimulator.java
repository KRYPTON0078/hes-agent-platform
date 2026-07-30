package com.hes.agent;

import java.util.LinkedHashMap;
import java.util.Map;

/** Simulates firmware OTA download/apply/ACK progression for protocol demos. */
public final class OtaAckSimulator {
    public Map<String, Object> nextAck(String jobCode, String phase) {
        Map<String, Object> ack = new LinkedHashMap<>();
        ack.put("jobCode", jobCode);
        ack.put("fromPhase", phase);
        String next = switch (phase) {
            case "CREATED" -> "DOWNLOADING";
            case "DOWNLOADING" -> "APPLYING";
            case "APPLYING" -> "ACKED";
            default -> phase;
        };
        ack.put("toPhase", next);
        ack.put("downloadOk", !"CREATED".equals(phase));
        ack.put("applyOk", "APPLYING".equals(phase) || "ACKED".equals(phase));
        ack.put("agentAck", "ACKED".equals(next));
        return ack;
    }
}