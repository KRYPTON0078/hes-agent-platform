package com.hes.server.security.iam;

import java.util.List;

public record TokenResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        long expiresInSeconds,
        List<String> roles
) {
}
