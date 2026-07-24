package com.hes.common.protocol;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;

/**
 * Home energy storage telemetry snapshot reported by an Agent.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record TelemetryPayload(
        BigDecimal socPercent,
        BigDecimal batteryKwh,
        BigDecimal inverterWatts,
        BigDecimal gridWatts,
        BigDecimal homeLoadWatts,
        BigDecimal batteryVoltage,
        BigDecimal batteryCurrent,
        Integer faultCode,
        String faultMessage,
        String operatingMode
) {
}
