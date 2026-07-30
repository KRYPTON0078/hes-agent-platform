package com.hes.server.observability;

public enum CriticalPath {
    AGENT_REGISTER,
    AGENT_TELEMETRY,
    COMMAND_ACK,
    OPS_AUTH,
    SCHEDULE_EVAL,
    DISPATCH_EVAL
}