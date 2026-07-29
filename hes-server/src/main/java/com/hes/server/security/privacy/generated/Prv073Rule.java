package com.hes.server.security.privacy.generated;

import com.hes.server.security.privacy.PrivacyRule;
import org.springframework.stereotype.Component;

@Component
public class Prv073Rule implements PrivacyRule {
    @Override public String id() { return "PRV-073"; }
    @Override public String dataClass() { return "AUDIT"; }
    @Override public int retentionDays() { return 226; }
    @Override public boolean redactInLogs() { return false; }
}