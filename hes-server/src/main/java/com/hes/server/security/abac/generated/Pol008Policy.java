package com.hes.server.security.abac.generated;

import com.hes.server.security.abac.AbacPolicy;
import com.hes.server.security.abac.AbacRequest;
import org.springframework.stereotype.Component;

@Component
public class Pol008Policy implements AbacPolicy {
    @Override public String id() { return "POL-008"; }
    @Override public String description() { return "Allow READ_TELEMETRY only when subject role includes ADMIN"; }
    @Override
    public boolean permits(AbacRequest request) {
        if (request == null || request.action() == null) { return false; }
        if (!"READ_TELEMETRY".equals(request.action())) { return true; }
        String role = request.attributes() == null ? null : request.attributes().get("role");
        return "ADMIN".equals(role);
    }
}