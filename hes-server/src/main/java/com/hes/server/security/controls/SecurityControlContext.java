package com.hes.server.security.controls;

import java.util.Map;

public record SecurityControlContext(
        String actor,
        String deviceId,
        String action,
        Map<String, Object> attributes
) {
    public static SecurityControlContext of(String actor, String deviceId, String action) {
        return new SecurityControlContext(actor, deviceId, action, Map.of());
    }
}
