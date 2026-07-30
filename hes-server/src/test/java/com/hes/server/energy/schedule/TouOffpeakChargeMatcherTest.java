package com.hes.server.energy.schedule;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class TouOffpeakChargeMatcherTest {
    private final TouOffpeakChargeMatcher matcher = new TouOffpeakChargeMatcher();

    @Test
    void matchesOffpeakWhenSocBelowCeiling() {
        ScheduleWindowEntity w = window(0, 360, BigDecimal.valueOf(90));
        ScheduleEvalContext ctx = new ScheduleEvalContext("D1", 120, 1, BigDecimal.valueOf(40), BigDecimal.ZERO, false);
        assertTrue(matcher.matches(w, ctx));
    }

    @Test
    void rejectsWhenSocAtCeiling() {
        ScheduleWindowEntity w = window(0, 360, BigDecimal.valueOf(90));
        ScheduleEvalContext ctx = new ScheduleEvalContext("D1", 120, 1, BigDecimal.valueOf(90), BigDecimal.ZERO, false);
        assertFalse(matcher.matches(w, ctx));
    }

    private ScheduleWindowEntity window(int start, int end, BigDecimal ceil) {
        ScheduleWindowEntity w = new ScheduleWindowEntity();
        w.setWindowType(ScheduleWindowType.TOU_OFFPEAK_CHARGE);
        w.setStartMinute(start);
        w.setEndMinute(end);
        w.setDayMask(127);
        w.setSocMax(ceil);
        w.setTargetMode(TargetOperatingMode.CHARGE);
        return w;
    }
}