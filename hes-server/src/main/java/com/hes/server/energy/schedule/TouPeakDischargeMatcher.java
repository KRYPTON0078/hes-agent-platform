package com.hes.server.energy.schedule;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class TouPeakDischargeMatcher implements ScheduleWindowMatcher {
    @Override
    public ScheduleWindowType supports() {
        return ScheduleWindowType.TOU_PEAK_DISCHARGE;
    }

    @Override
    public boolean matches(ScheduleWindowEntity window, ScheduleEvalContext context) {
        if ((window.getDayMask() & context.dayBit()) == 0) {
            return false;
        }
        int m = context.minuteOfDay();
        if (m < window.getStartMinute() || m >= window.getEndMinute()) {
            return false;
        }
        // Only discharge when SOC is above floor to avoid deep discharge in peak
        BigDecimal floor = window.getSocMin() == null ? BigDecimal.valueOf(20) : window.getSocMin();
        return context.socPercent() != null && context.socPercent().compareTo(floor) > 0;
    }
}
