package com.hes.server.security.abac.generated;

import com.hes.server.security.abac.AbacPolicy;
import com.hes.server.security.abac.AbacRequest;
import org.springframework.stereotype.Component;

@Component
public class Pol063Policy implements AbacPolicy {
    @Override public String id() { return "POL-063"; }
    @Override public String description() { return "Allow VIEW_FLEET only when subject role includes VIEWER"; }
    @Override
    public boolean permits(AbacRequest request) {
        if (request == null || request.action() == null) { return false; }
        if (!"VIEW_FLEET".equals(request.action())) { return true; }
        String role = request.attributes() == null ? null : request.attributes().get("role");
        return "VIEWER".equals(role);
    }
}