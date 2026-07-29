package com.hes.server.security.privacy.generated;

import com.hes.server.security.privacy.PrivacyRule;
import org.springframework.stereotype.Component;

@Component
public class Prv067Rule implements PrivacyRule {
    @Override public String id() { return "PRV-067"; }
    @Override public String dataClass() { return "DEVICE_KEY"; }
    @Override public int retentionDays() { return 208; }
    @Override public boolean redactInLogs() { return false; }
}