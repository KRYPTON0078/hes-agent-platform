package com.hes.server.energy.schedule;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import static org.junit.jupiter.api.Assertions.*;

class TouPeakDischargeMatcherTest {
    private final TouPeakDischargeMatcher matcher = new TouPeakDischargeMatcher();

    @Test
    void matchesPeakWindowWhenSocAboveFloor() {
        ScheduleWindowEntity w = window(1020, 1260, BigDecimal.valueOf(25));
        assertTrue(matcher.matches(w, ctx(18, 20, DayOfWeek.MONDAY, BigDecimal.valueOf(60), BigDecimal.ZERO, false)));
    }

    @Test
    void rejectsWhenSocAtOrBelowFloor() {
        ScheduleWindowEntity w = window(1020, 1260, BigDecimal.valueOf(25));
        assertFalse(matcher.matches(w, ctx(18, 20, DayOfWeek.MONDAY, BigDecimal.valueOf(25), BigDecimal.ZERO, false)));
    }

    private ScheduleWindowEntity window(int start, int end, BigDecimal floor) {
        ScheduleWindowEntity w = new ScheduleWindowEntity();
        w.setWindowType(ScheduleWindowType.TOU_PEAK_DISCHARGE);
        w.setStartMinute(start); w.setEndMinute(end); w.setDayMask(127);
        w.setSocMin(floor); w.setTargetMode(TargetOperatingMode.DISCHARGE);
        return w;
    }

    private static ScheduleEvalContext ctx(int hour, int minute, java.time.DayOfWeek dow, BigDecimal soc, BigDecimal exportW, boolean dr) {
        java.time.ZonedDateTime now = java.time.ZonedDateTime.of(2026, 7, 6, hour, minute, 0, 0, java.time.ZoneOffset.UTC)
                .with(java.time.temporal.TemporalAdjusters.nextOrSame(dow));
        return new ScheduleEvalContext("D1", now, soc, exportW, dr);
    }
}