package com.hes.server.energy.schedule;

public record ScheduleDecision(
        TargetOperatingMode mode,
        Long windowId,
        String reason,
        java.math.BigDecimal powerWatts
) {
    public static ScheduleDecision idle(String reason) {
        return new ScheduleDecision(TargetOperatingMode.IDLE, null, reason, null);
    }
}
