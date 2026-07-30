package com.hes.server.energy.schedule;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class DemandResponseMatcherTest {
    private final DemandResponseMatcher matcher = new DemandResponseMatcher();

    @Test
    void matchesWhenDrFlagActiveInWindow() {
        ScheduleWindowEntity w = new ScheduleWindowEntity();
        w.setWindowType(ScheduleWindowType.DEMAND_RESPONSE);
        w.setStartMinute(960); w.setEndMinute(1200); w.setDayMask(127);
        w.setTargetMode(TargetOperatingMode.DISCHARGE);
        assertTrue(matcher.matches(w, new ScheduleEvalContext("D1", 1000, 1, BigDecimal.valueOf(55), BigDecimal.ZERO, false, true)));
        assertFalse(matcher.matches(w, new ScheduleEvalContext("D1", 1000, 1, BigDecimal.valueOf(55), BigDecimal.ZERO, false, false)));
    }
}