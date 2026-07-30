package com.hes.server.energy.schedule;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ScheduleEngine {

    private final ChargeScheduleRepository scheduleRepository;
    private final ScheduleWindowRepository windowRepository;
    private final ScheduleExecutionRepository executionRepository;
    private final Map<ScheduleWindowType, ScheduleWindowMatcher> matchers;

    public ScheduleEngine(ChargeScheduleRepository scheduleRepository,
                          ScheduleWindowRepository windowRepository,
                          ScheduleExecutionRepository executionRepository,
                          List<ScheduleWindowMatcher> matcherList) {
        this.scheduleRepository = scheduleRepository;
        this.windowRepository = windowRepository;
        this.executionRepository = executionRepository;
        this.matchers = matcherList.stream()
                .collect(Collectors.toMap(ScheduleWindowMatcher::supports, Function.identity(), (a, b) -> a));
    }

    @Transactional
    public ScheduleDecision evaluateAndRecord(ScheduleEvalContext context) {
        List<ChargeScheduleEntity> schedules = scheduleRepository.findByDeviceIdAndEnabledTrue(context.deviceId());
        if (schedules.isEmpty()) {
            return ScheduleDecision.idle("No enabled schedule");
        }
        ScheduleDecision best = ScheduleDecision.idle("No matching window");
        Long bestScheduleId = null;
        int bestPriority = Integer.MAX_VALUE;

        for (ChargeScheduleEntity schedule : schedules) {
            List<ScheduleWindowEntity> windows = windowRepository.findByScheduleIdOrderByPriorityAsc(schedule.getId());
            for (ScheduleWindowEntity window : windows) {
                ScheduleWindowMatcher matcher = matchers.get(window.getWindowType());
                if (matcher == null || !matcher.matches(window, context)) {
                    continue;
                }
                if (window.getPriority() < bestPriority) {
                    bestPriority = window.getPriority();
                    bestScheduleId = schedule.getId();
                    best = new ScheduleDecision(
                            window.getTargetMode(),
                            window.getId(),
                            window.getWindowType().name() + " matched",
                            window.getPowerWatts()
                    );
                }
            }
        }

        if (bestScheduleId != null) {
            ScheduleExecutionEntity exec = new ScheduleExecutionEntity();
            exec.setScheduleId(bestScheduleId);
            exec.setWindowId(best.windowId());
            exec.setDeviceId(context.deviceId());
            exec.setDecidedMode(best.mode().name());
            exec.setReason(best.reason());
            executionRepository.save(exec);
        }
        return best;
    }
}
