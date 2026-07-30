package com.hes.agent;

import java.math.BigDecimal;
import java.time.LocalTime;

/**
 * Lightweight local schedule evaluator so the simulator honors TOU windows offline.
 */
public final class LocalScheduleEvaluator {
    private final int peakStartMinute;
    private final int peakEndMinute;
    private final BigDecimal socFloor;
    private final BigDecimal socCeiling;

    public LocalScheduleEvaluator(int peakStartMinute, int peakEndMinute, BigDecimal socFloor, BigDecimal socCeiling) {
        this.peakStartMinute = peakStartMinute;
        this.peakEndMinute = peakEndMinute;
        this.socFloor = socFloor;
        this.socCeiling = socCeiling;
    }

    public ScheduleDrivenMode evaluate(LocalTime now, BigDecimal soc) {
        int m = now.getHour() * 60 + now.getMinute();
        if (soc.compareTo(socFloor) < 0) {
            return ScheduleDrivenMode.CHARGING;
        }
        if (soc.compareTo(socCeiling) >= 0) {
            return ScheduleDrivenMode.STANDBY;
        }
        if (m >= peakStartMinute && m < peakEndMinute) {
            return ScheduleDrivenMode.DISCHARGING;
        }
        if (m < 360) {
            return ScheduleDrivenMode.CHARGING;
        }
        return ScheduleDrivenMode.IDLE;
    }
}