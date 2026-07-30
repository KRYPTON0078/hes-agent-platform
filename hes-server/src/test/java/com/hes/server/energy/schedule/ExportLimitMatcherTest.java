package com.hes.server.energy.schedule;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class ExportLimitMatcherTest {
    private final ExportLimitMatcher matcher = new ExportLimitMatcher();

    @Test
    void matchesWhenExportExceedsLimit() {
        ScheduleWindowEntity w = new ScheduleWindowEntity();
        w.setWindowType(ScheduleWindowType.EXPORT_LIMIT);
        w.setStartMinute(0); w.setEndMinute(1440); w.setDayMask(127);
        w.setPowerWatts(3000);
        w.setTargetMode(TargetOperatingMode.IDLE);
        ScheduleEvalContext over = new ScheduleEvalContext("D1", 700, 1, BigDecimal.valueOf(70), BigDecimal.valueOf(3500), false);
        ScheduleEvalContext under = new ScheduleEvalContext("D1", 700, 1, BigDecimal.valueOf(70), BigDecimal.valueOf(1000), false);
        assertTrue(matcher.matches(w, over));
        assertFalse(matcher.matches(w, under));
    }
}