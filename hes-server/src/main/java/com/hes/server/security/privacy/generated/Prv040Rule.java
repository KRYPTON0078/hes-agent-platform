package com.hes.server.security.privacy.generated;

import com.hes.server.security.privacy.PrivacyRule;
import org.springframework.stereotype.Component;

@Component
public class Prv040Rule implements PrivacyRule {
    @Override public String id() { return "PRV-040"; }
    @Override public String dataClass() { return "TELEMETRY"; }
    @Override public int retentionDays() { return 127; }
    @Override public boolean redactInLogs() { return true; }
}