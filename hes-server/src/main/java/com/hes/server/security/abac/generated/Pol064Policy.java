package com.hes.server.security.abac.generated;

import com.hes.server.security.abac.AbacPolicy;
import com.hes.server.security.abac.AbacRequest;
import org.springframework.stereotype.Component;

@Component
public class Pol064Policy implements AbacPolicy {
    @Override public String id() { return "POL-064"; }
    @Override public String description() { return "Allow READ_TELEMETRY only when subject role includes OPERATOR"; }
    @Override
    public boolean permits(AbacRequest request) {
        if (request == null || request.action() == null) { return false; }
        if (!"READ_TELEMETRY".equals(request.action())) { return true; }
        String role = request.attributes() == null ? null : request.attributes().get("role");
        return "OPERATOR".equals(role);
    }
}