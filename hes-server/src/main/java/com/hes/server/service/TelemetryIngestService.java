package com.hes.server.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hes.common.error.ErrorCode;
import com.hes.common.protocol.AgentMessage;
import com.hes.common.protocol.MessageType;
import com.hes.common.protocol.TelemetryPayload;
import com.hes.server.cache.TelemetrySnapshotCache;
import com.hes.server.domain.device.DeviceEntity;
import com.hes.server.domain.telemetry.TelemetryHistoryEntity;
import com.hes.server.domain.telemetry.TelemetryHistoryRepository;
import com.hes.server.domain.telemetry.TelemetryLatestEntity;
import com.hes.server.domain.telemetry.TelemetryLatestRepository;
import com.hes.server.messaging.AgentEventBus;
import com.hes.server.presence.OnlinePresenceStore;
import com.hes.server.web.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Service
public class TelemetryIngestService {

    private final DeviceRegistryService deviceRegistryService;
    private final TelemetryLatestRepository latestRepository;
    private final TelemetryHistoryRepository historyRepository;
    private final AgentEventBus eventBus;
    private final OnlinePresenceStore presenceStore;
    private final TelemetrySnapshotCache snapshotCache;
    private final AlertService alertService;
    private final ObjectMapper objectMapper;

    public TelemetryIngestService(DeviceRegistryService deviceRegistryService,
                                  TelemetryLatestRepository latestRepository,
                                  TelemetryHistoryRepository historyRepository,
                                  AgentEventBus eventBus,
                                  OnlinePresenceStore presenceStore,
                                  TelemetrySnapshotCache snapshotCache,
                                  AlertService alertService,
                                  ObjectMapper objectMapper) {
        this.deviceRegistryService = deviceRegistryService;
        this.latestRepository = latestRepository;
        this.historyRepository = historyRepository;
        this.eventBus = eventBus;
        this.presenceStore = presenceStore;
        this.snapshotCache = snapshotCache;
        this.alertService = alertService;
        this.objectMapper = objectMapper;
    }

    public AgentMessage accept(AgentMessage message) {
        if (message.type() != MessageType.TELEMETRY_REPORT) {
            throw new BusinessException(ErrorCode.PROTOCOL_UNSUPPORTED, "Expected TELEMETRY_REPORT");
        }
        deviceRegistryService.requireDevice(message.deviceId());
        eventBus.publishTelemetry(message);
        return AgentMessage.of(
                MessageType.TELEMETRY_ACK,
                UUID.randomUUID().toString(),
                message.deviceId(),
                Map.of("status", "ACCEPTED", "messageId", message.messageId())
        );
    }

    @Transactional
    public void persist(AgentMessage message) {
        DeviceEntity device = deviceRegistryService.requireDevice(message.deviceId());
        TelemetryPayload payload = objectMapper.convertValue(
                message.payload() == null ? Map.of() : message.payload(),
                TelemetryPayload.class
        );
        Instant reportedAt = message.timestamp() == null ? Instant.now() : message.timestamp();

        TelemetryHistoryEntity history = new TelemetryHistoryEntity();
        history.setDevice(device);
        copyPayload(history, payload, reportedAt);
        historyRepository.save(history);

        TelemetryLatestEntity latest = latestRepository.findById(device.getId()).orElseGet(TelemetryLatestEntity::new);
        latest.setDevice(device);
        copyPayload(latest, payload, reportedAt);
        latestRepository.save(latest);

        snapshotCache.put(device.getDeviceId(), payload);
        presenceStore.heartbeat(device.getDeviceId(), Instant.now());
        device.setLastSeenAt(Instant.now());
        alertService.evaluateTelemetry(device, payload);
    }

    private static void copyPayload(TelemetryHistoryEntity target, TelemetryPayload payload, Instant reportedAt) {
        target.setSocPercent(payload.socPercent());
        target.setBatteryKwh(payload.batteryKwh());
        target.setInverterWatts(payload.inverterWatts());
        target.setGridWatts(payload.gridWatts());
        target.setHomeLoadWatts(payload.homeLoadWatts());
        target.setBatteryVoltage(payload.batteryVoltage());
        target.setBatteryCurrent(payload.batteryCurrent());
        target.setFaultCode(payload.faultCode());
        target.setFaultMessage(payload.faultMessage());
        target.setOperatingMode(payload.operatingMode());
        target.setReportedAt(reportedAt);
    }

    private static void copyPayload(TelemetryLatestEntity target, TelemetryPayload payload, Instant reportedAt) {
        target.setSocPercent(payload.socPercent());
        target.setBatteryKwh(payload.batteryKwh());
        target.setInverterWatts(payload.inverterWatts());
        target.setGridWatts(payload.gridWatts());
        target.setHomeLoadWatts(payload.homeLoadWatts());
        target.setBatteryVoltage(payload.batteryVoltage());
        target.setBatteryCurrent(payload.batteryCurrent());
        target.setFaultCode(payload.faultCode());
        target.setFaultMessage(payload.faultMessage());
        target.setOperatingMode(payload.operatingMode());
        target.setReportedAt(reportedAt);
    }
}
