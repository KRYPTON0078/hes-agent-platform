package com.hes.server.security.privacy;

public interface PrivacyRule {
    String id();
    String dataClass();
    int retentionDays();
    boolean redactInLogs();
}
