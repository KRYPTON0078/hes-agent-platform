package com.hes.common.error;

public record ApiError(
        ErrorCode code,
        String message,
        String traceId
) {
}
