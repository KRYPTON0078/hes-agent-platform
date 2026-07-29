package com.hes.server.security.privacy.generated;

import com.hes.server.security.privacy.PrivacyRule;
import org.springframework.stereotype.Component;

@Component
public class Prv078Rule implements PrivacyRule {
    @Override public String id() { return "PRV-078"; }
    @Override public String dataClass() { return "ALERT"; }
    @Override public int retentionDays() { return 241; }
    @Override public boolean redactInLogs() { return true; }
}