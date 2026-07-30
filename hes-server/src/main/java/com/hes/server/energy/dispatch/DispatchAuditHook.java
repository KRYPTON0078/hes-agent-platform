package com.hes.server.energy.dispatch;

import com.hes.server.security.audit.SecurityAuditService;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class DispatchAuditHook {
    private final SecurityAuditService auditService;

    public DispatchAuditHook(SecurityAuditService auditService) {
        this.auditService = auditService;
    }

    public void onDecision(DispatchDecisionEntity decision) {
        auditService.record(
                "DISPATCH_DECISION",
                "dispatch-engine",
                decision.getDeviceId(),
                Map.of(
                        "action", decision.getDecidedAction(),
                        "reason", decision.getReason(),
                        "policyId", String.valueOf(decision.getPolicyId())
                ),
                null
        );
    }
}