package com.hes.server.energy.schedule;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class SocCeilingIdleMatcherTest {
    private final SocCeilingIdleMatcher matcher = new SocCeilingIdleMatcher();

    @Test
    void matchesWhenSocAtOrAboveCeiling() {
        ScheduleWindowEntity w = new ScheduleWindowEntity();
        w.setWindowType(ScheduleWindowType.SOC_CEILING_IDLE);
        w.setStartMinute(0); w.setEndMinute(1440); w.setDayMask(127);
        w.setSocMax(BigDecimal.valueOf(95));
        w.setTargetMode(TargetOperatingMode.IDLE);
        ScheduleEvalContext ctx = new ScheduleEvalContext("D1", 500, 1, BigDecimal.valueOf(96), BigDecimal.ZERO, false);
        assertTrue(matcher.matches(w, ctx));
    }
}