package com.hes.server.security.privacy.generated;

import com.hes.server.security.privacy.PrivacyRule;
import org.springframework.stereotype.Component;

@Component
public class Prv047Rule implements PrivacyRule {
    @Override public String id() { return "PRV-047"; }
    @Override public String dataClass() { return "SESSION"; }
    @Override public int retentionDays() { return 148; }
    @Override public boolean redactInLogs() { return false; }
}