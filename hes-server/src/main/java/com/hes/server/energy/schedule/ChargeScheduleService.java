package com.hes.server.energy.schedule;

import com.hes.common.error.ErrorCode;
import com.hes.server.web.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ChargeScheduleService {

    private final ChargeScheduleRepository scheduleRepository;
    private final ScheduleWindowRepository windowRepository;
    private final ScheduleExecutionRepository executionRepository;
    private final ScheduleEngine scheduleEngine;

    public ChargeScheduleService(ChargeScheduleRepository scheduleRepository,
                                 ScheduleWindowRepository windowRepository,
                                 ScheduleExecutionRepository executionRepository,
                                 ScheduleEngine scheduleEngine) {
        this.scheduleRepository = scheduleRepository;
        this.windowRepository = windowRepository;
        this.executionRepository = executionRepository;
        this.scheduleEngine = scheduleEngine;
    }

    @Transactional
    public ChargeScheduleEntity create(String code, String deviceId, String name, String timezone) {
        if (scheduleRepository.findByScheduleCode(code).isPresent()) {
            throw new BusinessException(ErrorCode.VALIDATION_FAILED, "schedule exists");
        }
        ChargeScheduleEntity entity = new ChargeScheduleEntity();
        entity.setScheduleCode(code);
        entity.setDeviceId(deviceId);
        entity.setName(name);
        entity.setTimezone(timezone == null ? "UTC" : timezone);
        return scheduleRepository.save(entity);
    }

    @Transactional
    public ScheduleWindowEntity addWindow(Long scheduleId, ScheduleWindowEntity window) {
        scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new BusinessException(ErrorCode.VALIDATION_FAILED, "schedule not found"));
        if (window.getStartMinute() < 0 || window.getEndMinute() > 1440 || window.getStartMinute() >= window.getEndMinute()) {
            throw new BusinessException(ErrorCode.VALIDATION_FAILED, "invalid minute range");
        }
        window.setScheduleId(scheduleId);
        return windowRepository.save(window);
    }

    public List<ChargeScheduleEntity> forDevice(String deviceId) {
        return scheduleRepository.findByDeviceIdAndEnabledTrue(deviceId);
    }

    public ScheduleDecision evaluate(ScheduleEvalContext context) {
        return scheduleEngine.evaluateAndRecord(context);
    }

    public List<ScheduleExecutionEntity> recent(String deviceId) {
        return executionRepository.findTop50ByDeviceIdOrderByExecutedAtDesc(deviceId);
    }
}
