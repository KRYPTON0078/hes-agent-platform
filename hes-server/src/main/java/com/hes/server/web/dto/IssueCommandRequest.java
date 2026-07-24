package com.hes.server.web.dto;

import com.hes.common.protocol.CommandType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record IssueCommandRequest(
        @NotNull CommandType commandType,
        Map<String, Object> params,
        @NotBlank String idempotencyKey,
        String requestedBy
) {
}
