package com.hes.server.security.abac.generated;

import com.hes.server.security.abac.AbacPolicy;
import com.hes.server.security.abac.AbacRequest;
import org.springframework.stereotype.Component;

@Component
public class Pol013Policy implements AbacPolicy {
    @Override public String id() { return "POL-013"; }
    @Override public String description() { return "Allow EXPORT_FORENSICS only when subject role includes OPERATOR"; }
    @Override
    public boolean permits(AbacRequest request) {
        if (request == null || request.action() == null) { return false; }
        if (!"EXPORT_FORENSICS".equals(request.action())) { return true; }
        String role = request.attributes() == null ? null : request.attributes().get("role");
        return "OPERATOR".equals(role);
    }
}