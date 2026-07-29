package com.hes.server.security.privacy.generated;

import com.hes.server.security.privacy.PrivacyRule;
import org.springframework.stereotype.Component;

@Component
public class Prv050Rule implements PrivacyRule {
    @Override public String id() { return "PRV-050"; }
    @Override public String dataClass() { return "OPS_USER"; }
    @Override public int retentionDays() { return 157; }
    @Override public boolean redactInLogs() { return true; }
}