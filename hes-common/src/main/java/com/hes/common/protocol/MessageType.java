package com.hes.common.protocol;

/**
 * Agent ↔ cloud message types for protocol v1.
 */
public enum MessageType {
    AGENT_REGISTER,
    AGENT_REGISTER_ACK,
    HEARTBEAT,
    HEARTBEAT_ACK,
    TELEMETRY_REPORT,
    TELEMETRY_ACK,
    COMMAND_DISPATCH,
    COMMAND_ACK,
    ERROR
}
