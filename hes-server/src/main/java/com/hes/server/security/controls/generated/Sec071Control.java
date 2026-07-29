package com.hes.server.security.controls.generated;

import com.hes.server.security.controls.ControlSeverity;
import com.hes.server.security.controls.SecurityControl;
import com.hes.server.security.controls.SecurityControlContext;
import org.springframework.stereotype.Component;

@Component
public class Sec071Control implements SecurityControl {
    @Override public String id() { return "SEC-071"; }
    @Override public String title() { return "Enforce EXPORT guard against ElevationOfPrivilege"; }
    @Override public String threat() { return "ElevationOfPrivilege"; }
    @Override public String mitigation() { return "Require authenticated principal and validate EXPORT attributes before execution"; }
    @Override public ControlSeverity severity() { return ControlSeverity.CRITICAL; }
    @Override
    public boolean evaluate(SecurityControlContext context) {
        if (context == null || context.action() == null) { return false; }
        if ("EXPORT".equals(context.action()) && (context.actor() == null || context.actor().isBlank())) {
            return false;
        }
        return true;
    }
}