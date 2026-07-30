package com.hes.server.energy.schedule;

import org.springframework.stereotype.Component;

@Component
public class DemandResponseMatcher implements ScheduleWindowMatcher {
    @Override
    public ScheduleWindowType supports() {
        return ScheduleWindowType.DEMAND_RESPONSE;
    }

    @Override
    public boolean matches(ScheduleWindowEntity window, ScheduleEvalContext context) {
        return context.demandResponseActive();
    }
}
