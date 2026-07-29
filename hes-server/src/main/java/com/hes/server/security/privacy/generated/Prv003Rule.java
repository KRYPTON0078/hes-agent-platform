package com.hes.server.security.privacy.generated;

import com.hes.server.security.privacy.PrivacyRule;
import org.springframework.stereotype.Component;

@Component
public class Prv003Rule implements PrivacyRule {
    @Override public String id() { return "PRV-003"; }
    @Override public String dataClass() { return "DEVICE_KEY"; }
    @Override public int retentionDays() { return 16; }
    @Override public boolean redactInLogs() { return false; }
}