package com.hes.server.energy.schedule;

public enum ScheduleWindowType {
    TOU_PEAK_DISCHARGE,
    TOU_OFFPEAK_CHARGE,
    SOC_FLOOR_CHARGE,
    SOC_CEILING_IDLE,
    WEEKEND_ECO,
    EXPORT_LIMIT,
    DEMAND_RESPONSE,
    MANUAL_OVERRIDE
}
