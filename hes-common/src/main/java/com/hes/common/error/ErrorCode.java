package com.hes.common.error;

/**
 * Stable API / protocol error codes for ops and Agent troubleshooting.
 */
public enum ErrorCode {
    VALIDATION_FAILED,
    DEVICE_NOT_FOUND,
    DEVICE_OFFLINE,
    UNAUTHORIZED,
    IDEMPOTENCY_CONFLICT,
    COMMAND_TIMEOUT,
    PROTOCOL_UNSUPPORTED,
    INTERNAL_ERROR
}
