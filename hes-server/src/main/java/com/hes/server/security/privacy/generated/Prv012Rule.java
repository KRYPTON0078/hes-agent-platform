package com.hes.server.security.privacy.generated;

import com.hes.server.security.privacy.PrivacyRule;
import org.springframework.stereotype.Component;

@Component
public class Prv012Rule implements PrivacyRule {
    @Override public String id() { return "PRV-012"; }
    @Override public String dataClass() { return "SITE_META"; }
    @Override public int retentionDays() { return 43; }
    @Override public boolean redactInLogs() { return true; }
}