package com.hes.server.energy.schedule;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import static org.junit.jupiter.api.Assertions.*;

class TouOffpeakChargeMatcherTest {
    private final TouOffpeakChargeMatcher matcher = new TouOffpeakChargeMatcher();

    @Test
    void matchesOffpeakWhenSocBelowCeiling() {
        ScheduleWindowEntity w = window(0, 360, BigDecimal.valueOf(90));
        assertTrue(matcher.matches(w, ctx(2, 0, DayOfWeek.TUESDAY, BigDecimal.valueOf(40), BigDecimal.ZERO, false)));
    }

    @Test
    void rejectsWhenSocAtCeiling() {
        ScheduleWindowEntity w = window(0, 360, BigDecimal.valueOf(90));
        assertFalse(matcher.matches(w, ctx(2, 0, DayOfWeek.TUESDAY, BigDecimal.valueOf(90), BigDecimal.ZERO, false)));
    }

    private ScheduleWindowEntity window(int start, int end, BigDecimal ceil) {
        ScheduleWindowEntity w = new ScheduleWindowEntity();
        w.setWindowType(ScheduleWindowType.TOU_OFFPEAK_CHARGE);
        w.setStartMinute(start); w.setEndMinute(end); w.setDayMask(127);
        w.setSocMax(ceil); w.setTargetMode(TargetOperatingMode.CHARGING);
        return w;
    }

    private static ScheduleEvalContext ctx(int hour, int minute, java.time.DayOfWeek dow, BigDecimal soc, BigDecimal exportW, boolean dr) {
        java.time.ZonedDateTime now = java.time.ZonedDateTime.of(2026, 7, 6, hour, minute, 0, 0, java.time.ZoneOffset.UTC)
                .with(java.time.temporal.TemporalAdjusters.nextOrSame(dow));
        return new ScheduleEvalContext("D1", now, soc, exportW, dr);
    }
}