package com.hes.server.energy.schedule;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class SocFloorChargeMatcher implements ScheduleWindowMatcher {
    @Override
    public ScheduleWindowType supports() {
        return ScheduleWindowType.SOC_FLOOR_CHARGE;
    }

    @Override
    public boolean matches(ScheduleWindowEntity window, ScheduleEvalContext context) {
        BigDecimal floor = window.getSocMin() == null ? BigDecimal.valueOf(15) : window.getSocMin();
        return context.socPercent() != null && context.socPercent().compareTo(floor) < 0;
    }
}
