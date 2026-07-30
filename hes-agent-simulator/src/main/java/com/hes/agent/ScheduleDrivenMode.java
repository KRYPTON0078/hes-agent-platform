package com.hes.agent;

/**
 * Local schedule-driven operating modes mirrored from HES schedule decisions.
 */
public enum ScheduleDrivenMode {
    IDLE,
    CHARGING,
    DISCHARGING,
    STANDBY,
    EXPORT_LIMITED;

    public static ScheduleDrivenMode fromServer(String mode) {
        if (mode == null || mode.isBlank()) {
            return IDLE;
        }
        try {
            return ScheduleDrivenMode.valueOf(mode.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            return IDLE;
        }
    }
}