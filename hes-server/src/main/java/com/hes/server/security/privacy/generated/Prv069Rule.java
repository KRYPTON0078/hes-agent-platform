package com.hes.server.security.privacy.generated;

import com.hes.server.security.privacy.PrivacyRule;
import org.springframework.stereotype.Component;

@Component
public class Prv069Rule implements PrivacyRule {
    @Override public String id() { return "PRV-069"; }
    @Override public String dataClass() { return "COMMAND_PAYLOAD"; }
    @Override public int retentionDays() { return 214; }
    @Override public boolean redactInLogs() { return false; }
}