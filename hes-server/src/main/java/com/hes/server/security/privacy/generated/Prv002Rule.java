package com.hes.server.security.privacy.generated;

import com.hes.server.security.privacy.PrivacyRule;
import org.springframework.stereotype.Component;

@Component
public class Prv002Rule implements PrivacyRule {
    @Override public String id() { return "PRV-002"; }
    @Override public String dataClass() { return "OPS_USER"; }
    @Override public int retentionDays() { return 13; }
    @Override public boolean redactInLogs() { return true; }
}