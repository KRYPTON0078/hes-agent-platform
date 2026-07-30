package com.hes.server.energy.schedule;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class WeekendEcoMatcherTest {
    private final WeekendEcoMatcher matcher = new WeekendEcoMatcher();

    @Test
    void matchesOnlyOnWeekend() {
        ScheduleWindowEntity w = new ScheduleWindowEntity();
        w.setWindowType(ScheduleWindowType.WEEKEND_ECO);
        w.setStartMinute(0); w.setEndMinute(1440); w.setDayMask(127);
        w.setTargetMode(TargetOperatingMode.SELF_CONSUME);
        assertTrue(matcher.matches(w, new ScheduleEvalContext("D1", 600, 64, BigDecimal.valueOf(50), BigDecimal.ZERO, true)));
        assertFalse(matcher.matches(w, new ScheduleEvalContext("D1", 600, 1, BigDecimal.valueOf(50), BigDecimal.ZERO, false)));
    }
}