package com.hes.server.security.audit;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ops/security/audit")
@Tag(name = "Security Audit")
public class SecurityAuditController {

    private final SecurityAuditService securityAuditService;

    public SecurityAuditController(SecurityAuditService securityAuditService) {
        this.securityAuditService = securityAuditService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<SecurityAuditEventEntity> latest() {
        return securityAuditService.latest();
    }

    @GetMapping("/type/{eventType}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<SecurityAuditEventEntity> byType(@PathVariable String eventType) {
        return securityAuditService.latestByType(eventType);
    }
}
