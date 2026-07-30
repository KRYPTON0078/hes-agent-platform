package com.hes.server.energy.schedule;

/**
 * Pluggable matcher for a schedule window type.
 */
public interface ScheduleWindowMatcher {
    ScheduleWindowType supports();
    boolean matches(ScheduleWindowEntity window, ScheduleEvalContext context);
}
