package com.hes.server.security.controls.generated;

import com.hes.server.security.controls.ControlSeverity;
import com.hes.server.security.controls.SecurityControl;
import com.hes.server.security.controls.SecurityControlContext;
import org.springframework.stereotype.Component;

@Component
public class Sec165Control implements SecurityControl {
    @Override public String id() { return "SEC-165"; }
    @Override public String title() { return "Enforce AUDIT_READ guard against InformationDisclosure"; }
    @Override public String threat() { return "InformationDisclosure"; }
    @Override public String mitigation() { return "Require authenticated principal and validate AUDIT_READ attributes before execution"; }
    @Override public ControlSeverity severity() { return ControlSeverity.MEDIUM; }
    @Override
    public boolean evaluate(SecurityControlContext context) {
        if (context == null || context.action() == null) { return false; }
        if ("AUDIT_READ".equals(context.action()) && (context.actor() == null || context.actor().isBlank())) {
            return false;
        }
        return true;
    }
}