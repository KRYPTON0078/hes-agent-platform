package com.hes.server.security.privacy.generated;

import com.hes.server.security.privacy.PrivacyRule;
import org.springframework.stereotype.Component;

@Component
public class Prv079Rule implements PrivacyRule {
    @Override public String id() { return "PRV-079"; }
    @Override public String dataClass() { return "SESSION"; }
    @Override public int retentionDays() { return 244; }
    @Override public boolean redactInLogs() { return false; }
}