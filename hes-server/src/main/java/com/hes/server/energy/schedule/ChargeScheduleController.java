package com.hes.server.energy.schedule;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ops/schedules")
@Tag(name = "Energy Schedules")
@PreAuthorize("hasAnyRole('OPERATOR','ADMIN')")
public class ChargeScheduleController {

    private final ChargeScheduleService chargeScheduleService;

    public ChargeScheduleController(ChargeScheduleService chargeScheduleService) {
        this.chargeScheduleService = chargeScheduleService;
    }

    public record CreateScheduleRequest(@NotBlank String scheduleCode, @NotBlank String deviceId,
                                        @NotBlank String name, String timezone) {
    }

    public record AddWindowRequest(@NotNull ScheduleWindowType windowType, int dayMask,
                                   int startMinute, int endMinute, @NotNull TargetOperatingMode targetMode,
                                   BigDecimal socMin, BigDecimal socMax, BigDecimal powerWatts, int priority) {
    }

    public record EvaluateRequest(@NotBlank String deviceId, BigDecimal socPercent,
                                  BigDecimal gridExportWatts, boolean demandResponseActive, String timezone) {
    }

    @PostMapping
    public ChargeScheduleEntity create(@RequestBody CreateScheduleRequest request) {
        return chargeScheduleService.create(request.scheduleCode(), request.deviceId(), request.name(), request.timezone());
    }

    @PostMapping("/{scheduleId}/windows")
    public ScheduleWindowEntity addWindow(@PathVariable Long scheduleId, @RequestBody AddWindowRequest request) {
        ScheduleWindowEntity window = new ScheduleWindowEntity();
        window.setWindowType(request.windowType());
        window.setDayMask(request.dayMask() == 0 ? 127 : request.dayMask());
        window.setStartMinute(request.startMinute());
        window.setEndMinute(request.endMinute());
        window.setTargetMode(request.targetMode());
        window.setSocMin(request.socMin());
        window.setSocMax(request.socMax());
        window.setPowerWatts(request.powerWatts());
        window.setPriority(request.priority() == 0 ? 100 : request.priority());
        return chargeScheduleService.addWindow(scheduleId, window);
    }

    @GetMapping("/device/{deviceId}")
    @PreAuthorize("hasAnyRole('VIEWER','OPERATOR','ADMIN')")
    public List<ChargeScheduleEntity> list(@PathVariable String deviceId) {
        return chargeScheduleService.forDevice(deviceId);
    }

    @PostMapping("/evaluate")
    public ScheduleDecision evaluate(@RequestBody EvaluateRequest request) {
        ZoneId zone = ZoneId.of(request.timezone() == null ? "UTC" : request.timezone());
        ScheduleEvalContext ctx = new ScheduleEvalContext(
                request.deviceId(),
                ZonedDateTime.now(zone),
                request.socPercent(),
                request.gridExportWatts(),
                request.demandResponseActive()
        );
        return chargeScheduleService.evaluate(ctx);
    }

    @GetMapping("/device/{deviceId}/executions")
    @PreAuthorize("hasAnyRole('VIEWER','OPERATOR','ADMIN')")
    public List<ScheduleExecutionEntity> executions(@PathVariable String deviceId) {
        return chargeScheduleService.recent(deviceId);
    }
}
