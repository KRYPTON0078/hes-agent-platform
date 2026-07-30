package com.hes.server.energy.schedule;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import static org.junit.jupiter.api.Assertions.*;

class WeekendEcoMatcherTest {
    private final WeekendEcoMatcher matcher = new WeekendEcoMatcher();

    @Test
    void matchesOnlyOnWeekend() {
        ScheduleWindowEntity w = new ScheduleWindowEntity();
        w.setWindowType(ScheduleWindowType.WEEKEND_ECO);
        w.setStartMinute(0); w.setEndMinute(1440); w.setDayMask(127);
        w.setTargetMode(TargetOperatingMode.SELF_CONSUME);
        assertTrue(matcher.matches(w, ctx(10, 0, DayOfWeek.SATURDAY, BigDecimal.valueOf(50), BigDecimal.ZERO, false)));
        assertFalse(matcher.matches(w, ctx(10, 0, DayOfWeek.MONDAY, BigDecimal.valueOf(50), BigDecimal.ZERO, false)));
    }

    private static ScheduleEvalContext ctx(int hour, int minute, java.time.DayOfWeek dow, BigDecimal soc, BigDecimal exportW, boolean dr) {
        java.time.ZonedDateTime now = java.time.ZonedDateTime.of(2026, 7, 6, hour, minute, 0, 0, java.time.ZoneOffset.UTC)
                .with(java.time.temporal.TemporalAdjusters.nextOrSame(dow));
        return new ScheduleEvalContext("D1", now, soc, exportW, dr);
    }
}