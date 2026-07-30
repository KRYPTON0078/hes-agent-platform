package com.hes.server.energy.schedule;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class TouPeakDischargeMatcherTest {
    private final TouPeakDischargeMatcher matcher = new TouPeakDischargeMatcher();

    @Test
    void matchesPeakWindowWhenSocAboveFloor() {
        ScheduleWindowEntity w = window(1020, 1260, BigDecimal.valueOf(25));
        ScheduleEvalContext ctx = new ScheduleEvalContext("D1", 1100, 1, BigDecimal.valueOf(60), BigDecimal.ZERO, false);
        assertTrue(matcher.matches(w, ctx));
    }

    @Test
    void rejectsWhenSocAtOrBelowFloor() {
        ScheduleWindowEntity w = window(1020, 1260, BigDecimal.valueOf(25));
        ScheduleEvalContext ctx = new ScheduleEvalContext("D1", 1100, 1, BigDecimal.valueOf(25), BigDecimal.ZERO, false);
        assertFalse(matcher.matches(w, ctx));
    }

    private ScheduleWindowEntity window(int start, int end, BigDecimal floor) {
        ScheduleWindowEntity w = new ScheduleWindowEntity();
        w.setWindowType(ScheduleWindowType.TOU_PEAK_DISCHARGE);
        w.setStartMinute(start);
        w.setEndMinute(end);
        w.setDayMask(127);
        w.setSocMin(floor);
        w.setTargetMode(TargetOperatingMode.DISCHARGE);
        return w;
    }
}