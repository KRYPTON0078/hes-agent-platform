package com.hes.server.web.dto;

import com.hes.common.protocol.CommandType;
import com.hes.common.protocol.TelemetryPayload;
import com.hes.server.domain.alert.AlertEntity;
import com.hes.server.domain.alert.AlertSeverity;
import com.hes.server.domain.alert.AlertStatus;
import com.hes.server.domain.command.CommandEntity;
import com.hes.server.domain.command.CommandStatus;
import com.hes.server.domain.device.DeviceEntity;
import com.hes.server.domain.device.DeviceStatus;
import com.hes.server.domain.telemetry.TelemetryHistoryEntity;
import com.hes.server.domain.telemetry.TelemetryLatestEntity;

import java.math.BigDecimal;
import java.time.Instant;

public final class OpsViews {

    private OpsViews() {
    }

    public record DeviceView(
            String deviceId,
            String siteCode,
            String model,
            String firmwareVersion,
            DeviceStatus status,
            Instant lastSeenAt,
            boolean online,
            TelemetryView telemetry
    ) {
        public static DeviceView from(DeviceEntity device, boolean online, TelemetryView telemetry) {
            String siteCode = device.getSite() == null ? null : device.getSite().getSiteCode();
            return new DeviceView(
                    device.getDeviceId(),
                    siteCode,
                    device.getModel(),
                    device.getFirmwareVersion(),
                    device.getStatus(),
                    device.getLastSeenAt(),
                    online,
                    telemetry
            );
        }
    }

    public record TelemetryView(
            BigDecimal socPercent,
            BigDecimal batteryKwh,
            BigDecimal inverterWatts,
            BigDecimal gridWatts,
            BigDecimal homeLoadWatts,
            BigDecimal batteryVoltage,
            BigDecimal batteryCurrent,
            Integer faultCode,
            String faultMessage,
            String operatingMode,
            Instant reportedAt
    ) {
        public static TelemetryView from(TelemetryLatestEntity e) {
            return new TelemetryView(
                    e.getSocPercent(), e.getBatteryKwh(), e.getInverterWatts(), e.getGridWatts(),
                    e.getHomeLoadWatts(), e.getBatteryVoltage(), e.getBatteryCurrent(),
                    e.getFaultCode(), e.getFaultMessage(), e.getOperatingMode(), e.getReportedAt()
            );
        }

        public static TelemetryView from(TelemetryHistoryEntity e) {
            return new TelemetryView(
                    e.getSocPercent(), e.getBatteryKwh(), e.getInverterWatts(), e.getGridWatts(),
                    e.getHomeLoadWatts(), e.getBatteryVoltage(), e.getBatteryCurrent(),
                    e.getFaultCode(), e.getFaultMessage(), e.getOperatingMode(), e.getReportedAt()
            );
        }

        public static TelemetryView from(TelemetryPayload p, Instant reportedAt) {
            return new TelemetryView(
                    p.socPercent(), p.batteryKwh(), p.inverterWatts(), p.gridWatts(),
                    p.homeLoadWatts(), p.batteryVoltage(), p.batteryCurrent(),
                    p.faultCode(), p.faultMessage(), p.operatingMode(), reportedAt
            );
        }
    }

    public record CommandView(
            String commandId,
            String deviceId,
            CommandType commandType,
            CommandStatus status,
            String idempotencyKey,
            String requestedBy,
            Instant timeoutAt,
            Instant createdAt,
            Instant updatedAt
    ) {
        public static CommandView from(CommandEntity c) {
            return new CommandView(
                    c.getCommandId(),
                    c.getDevice().getDeviceId(),
                    c.getCommandType(),
                    c.getStatus(),
                    c.getIdempotencyKey(),
                    c.getRequestedBy(),
                    c.getTimeoutAt(),
                    c.getCreatedAt(),
                    c.getUpdatedAt()
            );
        }
    }

    public record AlertView(
            Long id,
            String deviceId,
            String alertType,
            AlertSeverity severity,
            String message,
            AlertStatus status,
            Instant openedAt,
            Instant resolvedAt
    ) {
        public static AlertView from(AlertEntity a) {
            return new AlertView(
                    a.getId(),
                    a.getDevice().getDeviceId(),
                    a.getAlertType(),
                    a.getSeverity(),
                    a.getMessage(),
                    a.getStatus(),
                    a.getOpenedAt(),
                    a.getResolvedAt()
            );
        }
    }
}
