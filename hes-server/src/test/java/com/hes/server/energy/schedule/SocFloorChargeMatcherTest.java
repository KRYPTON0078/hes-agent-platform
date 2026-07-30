package com.hes.server.energy.schedule;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import static org.junit.jupiter.api.Assertions.*;

class SocFloorChargeMatcherTest {
    private final SocFloorChargeMatcher matcher = new SocFloorChargeMatcher();

    @Test
    void matchesWhenSocBelowFloor() {
        ScheduleWindowEntity w = new ScheduleWindowEntity();
        w.setWindowType(ScheduleWindowType.SOC_FLOOR_CHARGE);
        w.setStartMinute(0); w.setEndMinute(1440); w.setDayMask(127);
        w.setSocMin(BigDecimal.valueOf(20));
        w.setTargetMode(TargetOperatingMode.CHARGING);
        assertTrue(matcher.matches(w, ctx(8, 20, DayOfWeek.WEDNESDAY, BigDecimal.valueOf(15), BigDecimal.ZERO, false)));
    }

    private static ScheduleEvalContext ctx(int hour, int minute, java.time.DayOfWeek dow, BigDecimal soc, BigDecimal exportW, boolean dr) {
        java.time.ZonedDateTime now = java.time.ZonedDateTime.of(2026, 7, 6, hour, minute, 0, 0, java.time.ZoneOffset.UTC)
                .with(java.time.temporal.TemporalAdjusters.nextOrSame(dow));
        return new ScheduleEvalContext("D1", now, soc, exportW, dr);
    }
}