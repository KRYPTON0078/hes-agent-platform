package com.hes.server.security.iam;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterOpsUserRequest(
        @NotBlank @Size(min = 3, max = 64) String username,
        @NotBlank @Size(min = 12, max = 128) String password,
        String roleCode
) {
}
