package com.hes.server.security.controls;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class SecurityControlRegistryTest {

    @Test
    void failingReturnsControlsThatRejectBlankActorOnLogin() {
        SecurityControl loginGuard = new SecurityControl() {
            @Override public String id() { return "TEST"; }
            @Override public String title() { return "t"; }
            @Override public String threat() { return "Spoofing"; }
            @Override public String mitigation() { return "m"; }
            @Override public ControlSeverity severity() { return ControlSeverity.HIGH; }
            @Override public boolean evaluate(SecurityControlContext context) {
                return context.actor() != null && !context.actor().isBlank();
            }
        };
        SecurityControlRegistry registry = new SecurityControlRegistry(List.of(loginGuard));
        assertEquals(1, registry.failing(SecurityControlContext.of(" ", "d1", "LOGIN")).size());
        assertEquals(0, registry.failing(SecurityControlContext.of("ops", "d1", "LOGIN")).size());
    }
}
