package com.hes.server.web;

import com.hes.server.security.agentcred.AgentCredentialService;
import com.hes.server.security.audit.SecurityAuditService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/ops/devices/{deviceId}/credentials")
@Tag(name = "Agent Credentials")
public class AgentCredentialController {

    private final AgentCredentialService agentCredentialService;
    private final SecurityAuditService securityAuditService;

    public AgentCredentialController(AgentCredentialService agentCredentialService,
                                     SecurityAuditService securityAuditService) {
        this.agentCredentialService = agentCredentialService;
        this.securityAuditService = securityAuditService;
    }

    @PostMapping("/rotate")
    @PreAuthorize("hasAnyRole('OPERATOR','ADMIN')")
    public Map<String, Object> rotate(@PathVariable String deviceId, Authentication authentication) {
        Map<String, Object> result = agentCredentialService.rotate(deviceId);
        securityAuditService.record(
                "AGENT_KEY_ROTATED",
                authentication == null ? "unknown" : authentication.getName(),
                deviceId,
                Map.of("action", "rotate"),
                null
        );
        return result;
    }
}
