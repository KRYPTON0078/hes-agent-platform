package com.hes.agent;

/**
 * Entry point for the residential energy-storage Agent simulator.
 * Full register / heartbeat / telemetry / command loop lands in a later commit.
 */
public final class AgentSimulatorApp {

    private AgentSimulatorApp() {
    }

    public static void main(String[] args) {
        String deviceId = args.length > 0 ? args[0] : "HES-SIM-001";
        String baseUrl = args.length > 1 ? args[1] : "http://localhost:8080";
        System.out.printf("HES Agent Simulator scaffold ready. deviceId=%s baseUrl=%s%n", deviceId, baseUrl);
        System.out.println("Protocol: agent-v1 (REGISTER / HEARTBEAT / TELEMETRY / COMMAND)");
        System.out.println("Next: wire HTTP/MQTT client loop against hes-server.");
    }
}
