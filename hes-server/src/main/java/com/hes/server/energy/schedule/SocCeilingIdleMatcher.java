package com.hes.server.energy.schedule;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class SocCeilingIdleMatcher implements ScheduleWindowMatcher {
    @Override
    public ScheduleWindowType supports() {
        return ScheduleWindowType.SOC_CEILING_IDLE;
    }

    @Override
    public boolean matches(ScheduleWindowEntity window, ScheduleEvalContext context) {
        BigDecimal ceiling = window.getSocMax() == null ? BigDecimal.valueOf(98) : window.getSocMax();
        return context.socPercent() != null && context.socPercent().compareTo(ceiling) >= 0;
    }
}
