package com.hes.server.energy.schedule;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import static org.junit.jupiter.api.Assertions.*;

class DemandResponseMatcherTest {
    private final DemandResponseMatcher matcher = new DemandResponseMatcher();

    @Test
    void matchesWhenDrFlagActive() {
        ScheduleWindowEntity w = new ScheduleWindowEntity();
        w.setWindowType(ScheduleWindowType.DEMAND_RESPONSE);
        w.setStartMinute(960); w.setEndMinute(1200); w.setDayMask(127);
        w.setTargetMode(TargetOperatingMode.DISCHARGING);
        assertTrue(matcher.matches(w, ctx(17, 0, DayOfWeek.MONDAY, BigDecimal.valueOf(55), BigDecimal.ZERO, true)));
        assertFalse(matcher.matches(w, ctx(17, 0, DayOfWeek.MONDAY, BigDecimal.valueOf(55), BigDecimal.ZERO, false)));
    }

    private static ScheduleEvalContext ctx(int hour, int minute, java.time.DayOfWeek dow, BigDecimal soc, BigDecimal exportW, boolean dr) {
        java.time.ZonedDateTime now = java.time.ZonedDateTime.of(2026, 7, 6, hour, minute, 0, 0, java.time.ZoneOffset.UTC)
                .with(java.time.temporal.TemporalAdjusters.nextOrSame(dow));
        return new ScheduleEvalContext("D1", now, soc, exportW, dr);
    }
}