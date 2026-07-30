package com.hes.server.energy.schedule;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * Context for evaluating which schedule window should drive the Agent mode.
 */
public record ScheduleEvalContext(
        String deviceId,
        ZonedDateTime now,
        BigDecimal socPercent,
        BigDecimal gridExportWatts,
        boolean demandResponseActive
) {
    public int minuteOfDay() {
        return now.getHour() * 60 + now.getMinute();
    }

    public int dayBit() {
        // Monday=1 ... Sunday=64 aligned to ISO
        int iso = now.getDayOfWeek().getValue(); // 1=Mon..7=Sun
        return 1 << (iso - 1);
    }
}
