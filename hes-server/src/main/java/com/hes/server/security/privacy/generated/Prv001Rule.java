package com.hes.server.security.privacy.generated;

import com.hes.server.security.privacy.PrivacyRule;
import org.springframework.stereotype.Component;

@Component
public class Prv001Rule implements PrivacyRule {
    @Override public String id() { return "PRV-001"; }
    @Override public String dataClass() { return "AUDIT"; }
    @Override public int retentionDays() { return 10; }
    @Override public boolean redactInLogs() { return false; }
}