package com.hes.server.security.controls;

/**
 * Catalog entry for a concrete security control (NIST/CIS-inspired definitions).
 */
public interface SecurityControl {
    String id();
    String title();
    String threat();
    String mitigation();
    ControlSeverity severity();
    boolean evaluate(SecurityControlContext context);
}
