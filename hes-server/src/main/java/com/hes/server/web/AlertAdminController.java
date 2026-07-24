package com.hes.server.web;

import com.hes.server.domain.alert.AlertEntity;
import com.hes.server.domain.alert.AlertRepository;
import com.hes.server.domain.alert.AlertStatus;
import com.hes.common.error.ErrorCode;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ops/alerts")
@Tag(name = "Alert Admin")
public class AlertAdminController {
    private final AlertRepository alertRepository;

    public AlertAdminController(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    @PostMapping("/{alertId}/resolve")
    public Map<String, Object> resolve(@PathVariable Long alertId) {
        AlertEntity alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new BusinessException(ErrorCode.VALIDATION_FAILED, "Alert not found"));
        alert.setStatus(AlertStatus.RESOLVED);
        alert.setResolvedAt(Instant.now());
        alertRepository.save(alert);
        return Map.of("id", alertId, "status", alert.getStatus());
    }
}
