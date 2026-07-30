package com.hes.server.energy.schedule;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class SocFloorChargeMatcherTest {
    private final SocFloorChargeMatcher matcher = new SocFloorChargeMatcher();

    @Test
    void matchesWhenSocBelowFloor() {
        ScheduleWindowEntity w = new ScheduleWindowEntity();
        w.setWindowType(ScheduleWindowType.SOC_FLOOR_CHARGE);
        w.setStartMinute(0); w.setEndMinute(1440); w.setDayMask(127);
        w.setSocMin(BigDecimal.valueOf(20));
        w.setTargetMode(TargetOperatingMode.CHARGE);
        ScheduleEvalContext ctx = new ScheduleEvalContext("D1", 500, 1, BigDecimal.valueOf(15), BigDecimal.ZERO, false);
        assertTrue(matcher.matches(w, ctx));
    }
}