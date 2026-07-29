package com.hes.server.security.privacy.generated;

import com.hes.server.security.privacy.PrivacyRule;
import org.springframework.stereotype.Component;

@Component
public class Prv014Rule implements PrivacyRule {
    @Override public String id() { return "PRV-014"; }
    @Override public String dataClass() { return "ALERT"; }
    @Override public int retentionDays() { return 49; }
    @Override public boolean redactInLogs() { return true; }
}