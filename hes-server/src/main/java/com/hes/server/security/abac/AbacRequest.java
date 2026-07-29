package com.hes.server.security.abac;

import java.util.Map;

public record AbacRequest(
        String subject,
        String action,
        String resource,
        Map<String, String> attributes
) {
}
