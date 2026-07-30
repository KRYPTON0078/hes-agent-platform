package com.hes.server.energy.schedule;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import static org.junit.jupiter.api.Assertions.*;

class ExportLimitMatcherTest {
    private final ExportLimitMatcher matcher = new ExportLimitMatcher();

    @Test
    void matchesWhenExportExceedsLimit() {
        ScheduleWindowEntity w = new ScheduleWindowEntity();
        w.setWindowType(ScheduleWindowType.EXPORT_LIMIT);
        w.setStartMinute(0); w.setEndMinute(1440); w.setDayMask(127);
        w.setPowerWatts(BigDecimal.valueOf(3000));
        w.setTargetMode(TargetOperatingMode.IDLE);
        assertTrue(matcher.matches(w, ctx(11, 40, DayOfWeek.FRIDAY, BigDecimal.valueOf(70), BigDecimal.valueOf(3500), false)));
        assertFalse(matcher.matches(w, ctx(11, 40, DayOfWeek.FRIDAY, BigDecimal.valueOf(70), BigDecimal.valueOf(1000), false)));
    }

    private static ScheduleEvalContext ctx(int hour, int minute, java.time.DayOfWeek dow, BigDecimal soc, BigDecimal exportW, boolean dr) {
        java.time.ZonedDateTime now = java.time.ZonedDateTime.of(2026, 7, 6, hour, minute, 0, 0, java.time.ZoneOffset.UTC)
                .with(java.time.temporal.TemporalAdjusters.nextOrSame(dow));
        return new ScheduleEvalContext("D1", now, soc, exportW, dr);
    }
}