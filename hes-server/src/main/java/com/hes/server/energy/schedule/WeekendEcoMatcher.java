package com.hes.server.energy.schedule;

import org.springframework.stereotype.Component;

@Component
public class WeekendEcoMatcher implements ScheduleWindowMatcher {
    @Override
    public ScheduleWindowType supports() {
        return ScheduleWindowType.WEEKEND_ECO;
    }

    @Override
    public boolean matches(ScheduleWindowEntity window, ScheduleEvalContext context) {
        int iso = context.now().getDayOfWeek().getValue();
        if (iso < 6) {
            return false; // weekdays excluded
        }
        int m = context.minuteOfDay();
        return m >= window.getStartMinute() && m < window.getEndMinute();
    }
}
