package com.hes.server.security.abac;

public interface AbacPolicy {
    String id();
    String description();
    boolean permits(AbacRequest request);
}
