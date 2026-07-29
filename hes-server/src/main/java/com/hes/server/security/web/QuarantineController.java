package com.hes.server.security.web;

import com.hes.server.security.incident.DeviceQuarantineService;
import com.hes.server.security.audit.SecurityAuditService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ops/security/quarantine")
@Tag(name = "Incident Response")
public class QuarantineController {

    private final DeviceQuarantineService quarantineService;
    private final SecurityAuditService auditService;

    public QuarantineController(DeviceQuarantineService quarantineService, SecurityAuditService auditService) {
        this.quarantineService = quarantineService;
        this.auditService = auditService;
    }

    @PostMapping("/{deviceId}")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> quarantine(@PathVariable String deviceId,
                                          @RequestBody(required = false) Map<String, String> body,
                                          Authentication authentication) {
        String reason = body == null ? "manual" : body.getOrDefault("reason", "manual");
        quarantineService.quarantine(deviceId, reason);
        auditService.record("DEVICE_QUARANTINED", authentication.getName(), deviceId, Map.of("reason", reason), null);
        return Map.of("deviceId", deviceId, "quarantined", true);
    }

    @DeleteMapping("/{deviceId}")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> release(@PathVariable String deviceId, Authentication authentication) {
        quarantineService.release(deviceId);
        auditService.record("DEVICE_QUARANTINE_RELEASED", authentication.getName(), deviceId, Map.of(), null);
        return Map.of("deviceId", deviceId, "quarantined", false);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<String> list() {
        return quarantineService.allQuarantined();
    }
}
