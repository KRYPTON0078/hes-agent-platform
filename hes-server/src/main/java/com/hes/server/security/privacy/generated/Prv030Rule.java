package com.hes.server.security.privacy.generated;

import com.hes.server.security.privacy.PrivacyRule;
import org.springframework.stereotype.Component;

@Component
public class Prv030Rule implements PrivacyRule {
    @Override public String id() { return "PRV-030"; }
    @Override public String dataClass() { return "ALERT"; }
    @Override public int retentionDays() { return 97; }
    @Override public boolean redactInLogs() { return true; }
}