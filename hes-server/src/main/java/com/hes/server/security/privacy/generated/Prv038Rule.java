package com.hes.server.security.privacy.generated;

import com.hes.server.security.privacy.PrivacyRule;
import org.springframework.stereotype.Component;

@Component
public class Prv038Rule implements PrivacyRule {
    @Override public String id() { return "PRV-038"; }
    @Override public String dataClass() { return "ALERT"; }
    @Override public int retentionDays() { return 121; }
    @Override public boolean redactInLogs() { return true; }
}