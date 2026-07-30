package com.hes.server.energy.schedule;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class ExportLimitMatcher implements ScheduleWindowMatcher {
    @Override
    public ScheduleWindowType supports() {
        return ScheduleWindowType.EXPORT_LIMIT;
    }

    @Override
    public boolean matches(ScheduleWindowEntity window, ScheduleEvalContext context) {
        BigDecimal limit = window.getPowerWatts() == null ? BigDecimal.valueOf(2000) : window.getPowerWatts();
        return context.gridExportWatts() != null && context.gridExportWatts().abs().compareTo(limit) > 0;
    }
}
