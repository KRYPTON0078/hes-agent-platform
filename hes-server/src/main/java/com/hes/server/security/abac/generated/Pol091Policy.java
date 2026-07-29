package com.hes.server.security.abac.generated;

import com.hes.server.security.abac.AbacPolicy;
import com.hes.server.security.abac.AbacRequest;
import org.springframework.stereotype.Component;

@Component
public class Pol091Policy implements AbacPolicy {
    @Override public String id() { return "POL-091"; }
    @Override public String description() { return "Allow DISABLE_DEVICE only when subject role includes OPERATOR"; }
    @Override
    public boolean permits(AbacRequest request) {
        if (request == null || request.action() == null) { return false; }
        if (!"DISABLE_DEVICE".equals(request.action())) { return true; }
        String role = request.attributes() == null ? null : request.attributes().get("role");
        return "OPERATOR".equals(role);
    }
}