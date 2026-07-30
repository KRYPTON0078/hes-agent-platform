package com.hes.server.energy.schedule;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class TouOffpeakChargeMatcher implements ScheduleWindowMatcher {
    @Override
    public ScheduleWindowType supports() {
        return ScheduleWindowType.TOU_OFFPEAK_CHARGE;
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
        BigDecimal ceiling = window.getSocMax() == null ? BigDecimal.valueOf(95) : window.getSocMax();
        return context.socPercent() != null && context.socPercent().compareTo(ceiling) < 0;
    }
}
