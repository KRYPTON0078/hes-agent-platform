package com.hes.server.service;

import com.hes.common.protocol.TelemetryPayload;
import com.hes.server.config.HesProperties;
import com.hes.server.domain.alert.*;
import com.hes.server.domain.device.DeviceEntity;
import com.hes.server.domain.device.DeviceRepository;
import com.hes.server.domain.device.DeviceStatus;
import com.hes.server.presence.OnlinePresenceStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class AlertService {

    public static final String TYPE_LOW_SOC = "LOW_SOC";
    public static final String TYPE_FAULT = "DEVICE_FAULT";
    public static final String TYPE_OFFLINE = "DEVICE_OFFLINE";

    private final AlertRepository alertRepository;
    private final DeviceRepository deviceRepository;
    private final OnlinePresenceStore presenceStore;
    private final HesProperties properties;

    public AlertService(AlertRepository alertRepository,
                        DeviceRepository deviceRepository,
                        OnlinePresenceStore presenceStore,
                        HesProperties properties) {
        this.alertRepository = alertRepository;
        this.deviceRepository = deviceRepository;
        this.presenceStore = presenceStore;
        this.properties = properties;
    }

    @Transactional
    public void evaluateTelemetry(DeviceEntity device, TelemetryPayload payload) {
        if (payload.socPercent() != null
                && payload.socPercent().doubleValue() < properties.getAlert().getLowSocThreshold()) {
            openIfAbsent(device, TYPE_LOW_SOC, AlertSeverity.WARNING,
                    "SOC below threshold: " + payload.socPercent() + "%");
        } else {
            resolve(device, TYPE_LOW_SOC);
        }

        if (payload.faultCode() != null && payload.faultCode() != 0) {
            openIfAbsent(device, TYPE_FAULT, AlertSeverity.CRITICAL,
                    "Fault " + payload.faultCode() + ": " + payload.faultMessage());
        } else {
            resolve(device, TYPE_FAULT);
        }

        resolve(device, TYPE_OFFLINE);
    }

    @Transactional
    public int scanOfflineDevices() {
        int opened = 0;
        for (DeviceEntity device : deviceRepository.findAll()) {
            if (device.getStatus() == DeviceStatus.DISABLED) {
                continue;
            }
            if (!presenceStore.isOnline(device.getDeviceId())) {
                if (device.getStatus() != DeviceStatus.OFFLINE) {
                    device.setStatus(DeviceStatus.OFFLINE);
                    deviceRepository.save(device);
                }
                if (openIfAbsent(device, TYPE_OFFLINE, AlertSeverity.WARNING, "Device heartbeat expired")) {
                    opened++;
                }
            }
        }
        return opened;
    }

    public List<AlertEntity> openAlerts() {
        return alertRepository.findByStatusOrderByOpenedAtDesc(AlertStatus.OPEN);
    }

    private boolean openIfAbsent(DeviceEntity device, String type, AlertSeverity severity, String message) {
        return alertRepository.findFirstByDevice_IdAndAlertTypeAndStatus(device.getId(), type, AlertStatus.OPEN)
                .map(existing -> false)
                .orElseGet(() -> {
                    AlertEntity alert = new AlertEntity();
                    alert.setDevice(device);
                    alert.setAlertType(type);
                    alert.setSeverity(severity);
                    alert.setMessage(message);
                    alert.setStatus(AlertStatus.OPEN);
                    alert.setOpenedAt(Instant.now());
                    alertRepository.save(alert);
                    return true;
                });
    }

    private void resolve(DeviceEntity device, String type) {
        alertRepository.findFirstByDevice_IdAndAlertTypeAndStatus(device.getId(), type, AlertStatus.OPEN)
                .ifPresent(alert -> {
                    alert.setStatus(AlertStatus.RESOLVED);
                    alert.setResolvedAt(Instant.now());
                    alertRepository.save(alert);
                });
    }
}
