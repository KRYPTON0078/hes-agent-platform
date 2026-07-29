package com.hes.server.security.privacy.generated;

import com.hes.server.security.privacy.PrivacyRule;
import org.springframework.stereotype.Component;

@Component
public class Prv043Rule implements PrivacyRule {
    @Override public String id() { return "PRV-043"; }
    @Override public String dataClass() { return "DEVICE_KEY"; }
    @Override public int retentionDays() { return 136; }
    @Override public boolean redactInLogs() { return false; }
}