package com.hes.server.web;

import com.hes.server.cache.TelemetrySnapshotCache;
import com.hes.server.domain.alert.AlertEntity;
import com.hes.server.domain.command.CommandEntity;
import com.hes.server.domain.device.DeviceEntity;
import com.hes.server.domain.device.DeviceRepository;
import com.hes.server.domain.telemetry.TelemetryHistoryRepository;
import com.hes.server.domain.telemetry.TelemetryLatestRepository;
import com.hes.server.presence.OnlinePresenceStore;
import com.hes.server.service.AlertService;
import com.hes.server.service.CommandService;
import com.hes.server.service.DeviceRegistryService;
import com.hes.server.web.dto.IssueCommandRequest;
import com.hes.server.web.dto.OpsViews;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ops")
@Tag(name = "Ops / O&M")
@Transactional(readOnly = true)
@PreAuthorize("hasAnyRole('VIEWER','OPERATOR','ADMIN')")
public class OpsController {

    private final DeviceRepository deviceRepository;
    private final DeviceRegistryService deviceRegistryService;
    private final TelemetryLatestRepository telemetryLatestRepository;
    private final TelemetryHistoryRepository telemetryHistoryRepository;
    private final CommandService commandService;
    private final OnlinePresenceStore presenceStore;
    private final TelemetrySnapshotCache snapshotCache;
    private final AlertService alertService;

    public OpsController(DeviceRepository deviceRepository,
                         DeviceRegistryService deviceRegistryService,
                         TelemetryLatestRepository telemetryLatestRepository,
                         TelemetryHistoryRepository telemetryHistoryRepository,
                         CommandService commandService,
                         OnlinePresenceStore presenceStore,
                         TelemetrySnapshotCache snapshotCache,
                         AlertService alertService) {
        this.deviceRepository = deviceRepository;
        this.deviceRegistryService = deviceRegistryService;
        this.telemetryLatestRepository = telemetryLatestRepository;
        this.telemetryHistoryRepository = telemetryHistoryRepository;
        this.commandService = commandService;
        this.presenceStore = presenceStore;
        this.snapshotCache = snapshotCache;
        this.alertService = alertService;
    }

    @GetMapping("/fleet")
    @Operation(summary = "Fleet overview for O&M dashboard")
    public Map<String, Object> fleet() {
        Map<String, Object> body = new HashMap<>();
        body.put("deviceCount", deviceRepository.count());
        body.put("onlineCount", presenceStore.onlineCount());
        body.put("openAlerts", alertService.openAlerts().size());
        body.put("onlineDeviceIds", presenceStore.onlineDeviceIds());
        return body;
    }

    @GetMapping("/devices")
    public List<OpsViews.DeviceView> devices() {
        return deviceRepository.findAll().stream()
                .map(d -> OpsViews.DeviceView.from(d, presenceStore.isOnline(d.getDeviceId()), null))
                .toList();
    }

    @GetMapping("/devices/{deviceId}")
    public OpsViews.DeviceView device(@PathVariable String deviceId) {
        DeviceEntity device = deviceRegistryService.requireDevice(deviceId);
        OpsViews.TelemetryView telemetry = snapshotCache.get(deviceId)
                .map(p -> OpsViews.TelemetryView.from(p, Instant.now()))
                .orElseGet(() -> telemetryLatestRepository.findById(device.getId())
                        .map(OpsViews.TelemetryView::from)
                        .orElse(null));
        return OpsViews.DeviceView.from(device, presenceStore.isOnline(deviceId), telemetry);
    }

    @GetMapping("/devices/{deviceId}/telemetry")
    public List<OpsViews.TelemetryView> telemetryHistory(
            @PathVariable String deviceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to
    ) {
        deviceRegistryService.requireDevice(deviceId);
        return telemetryHistoryRepository.findRange(deviceId, from, to).stream()
                .map(OpsViews.TelemetryView::from)
                .toList();
    }

    @PostMapping("/devices/{deviceId}/commands")
    @Transactional
    public OpsViews.CommandView issueCommand(@PathVariable String deviceId,
                                             @Valid @RequestBody IssueCommandRequest request) {
        CommandEntity command = commandService.issue(
                deviceId,
                request.commandType(),
                request.params(),
                request.idempotencyKey(),
                request.requestedBy()
        );
        return OpsViews.CommandView.from(command);
    }

    @GetMapping("/commands/{commandId}")
    public OpsViews.CommandView command(@PathVariable String commandId) {
        return OpsViews.CommandView.from(commandService.get(commandId));
    }

    @GetMapping("/alerts")
    public List<OpsViews.AlertView> alerts() {
        return alertService.openAlerts().stream().map(OpsViews.AlertView::from).toList();
    }
}
