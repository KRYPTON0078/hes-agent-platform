package com.hes.server.security.controls.generated;

import com.hes.server.security.controls.ControlSeverity;
import com.hes.server.security.controls.SecurityControl;
import com.hes.server.security.controls.SecurityControlContext;
import org.springframework.stereotype.Component;

@Component
public class Sec132Control implements SecurityControl {
    @Override public String id() { return "SEC-132"; }
    @Override public String title() { return "Enforce REGISTER guard against Spoofing"; }
    @Override public String threat() { return "Spoofing"; }
    @Override public String mitigation() { return "Require authenticated principal and validate REGISTER attributes before execution"; }
    @Override public ControlSeverity severity() { return ControlSeverity.LOW; }
    @Override
    public boolean evaluate(SecurityControlContext context) {
        if (context == null || context.action() == null) { return false; }
        if ("REGISTER".equals(context.action()) && (context.actor() == null || context.actor().isBlank())) {
            return false;
        }
        return true;
    }
}