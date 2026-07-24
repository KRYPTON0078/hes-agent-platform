package com.hes.server.domain.command;

public enum CommandStatus {
    PENDING,
    DISPATCHED,
    ACKED,
    FAILED,
    TIMEOUT
}
