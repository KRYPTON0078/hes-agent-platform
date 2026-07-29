package com.hes.server.security.controls.generated;

import com.hes.server.security.controls.ControlSeverity;
import com.hes.server.security.controls.SecurityControl;
import com.hes.server.security.controls.SecurityControlContext;
import org.springframework.stereotype.Component;

@Component
public class Sec006Control implements SecurityControl {
    @Override public String id() { return "SEC-006"; }
    @Override public String title() { return "Enforce DISABLE_DEVICE guard against Spoofing"; }
    @Override public String threat() { return "Spoofing"; }
    @Override public String mitigation() { return "Require authenticated principal and validate DISABLE_DEVICE attributes before execution"; }
    @Override public ControlSeverity severity() { return ControlSeverity.HIGH; }
    @Override
    public boolean evaluate(SecurityControlContext context) {
        if (context == null || context.action() == null) { return false; }
        if ("DISABLE_DEVICE".equals(context.action()) && (context.actor() == null || context.actor().isBlank())) {
            return false;
        }
        return true;
    }
}