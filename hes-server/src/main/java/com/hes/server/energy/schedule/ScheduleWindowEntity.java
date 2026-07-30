package com.hes.server.energy.schedule;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "schedule_window")
public class ScheduleWindowEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "schedule_id", nullable = false)
    private Long scheduleId;

    @Enumerated(EnumType.STRING)
    @Column(name = "window_type", nullable = false, length = 32)
    private ScheduleWindowType windowType;

    @Column(name = "day_mask", nullable = false)
    private int dayMask = 127;

    @Column(name = "start_minute", nullable = false)
    private int startMinute;

    @Column(name = "end_minute", nullable = false)
    private int endMinute;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_mode", nullable = false, length = 32)
    private TargetOperatingMode targetMode;

    @Column(name = "soc_min", precision = 5, scale = 2)
    private BigDecimal socMin;

    @Column(name = "soc_max", precision = 5, scale = 2)
    private BigDecimal socMax;

    @Column(name = "power_watts", precision = 12, scale = 2)
    private BigDecimal powerWatts;

    @Column(nullable = false)
    private int priority = 100;

    public Long getId() { return id; }
    public Long getScheduleId() { return scheduleId; }
    public void setScheduleId(Long scheduleId) { this.scheduleId = scheduleId; }
    public ScheduleWindowType getWindowType() { return windowType; }
    public void setWindowType(ScheduleWindowType windowType) { this.windowType = windowType; }
    public int getDayMask() { return dayMask; }
    public void setDayMask(int dayMask) { this.dayMask = dayMask; }
    public int getStartMinute() { return startMinute; }
    public void setStartMinute(int startMinute) { this.startMinute = startMinute; }
    public int getEndMinute() { return endMinute; }
    public void setEndMinute(int endMinute) { this.endMinute = endMinute; }
    public TargetOperatingMode getTargetMode() { return targetMode; }
    public void setTargetMode(TargetOperatingMode targetMode) { this.targetMode = targetMode; }
    public BigDecimal getSocMin() { return socMin; }
    public void setSocMin(BigDecimal socMin) { this.socMin = socMin; }
    public BigDecimal getSocMax() { return socMax; }
    public void setSocMax(BigDecimal socMax) { this.socMax = socMax; }
    public BigDecimal getPowerWatts() { return powerWatts; }
    public void setPowerWatts(BigDecimal powerWatts) { this.powerWatts = powerWatts; }
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
}
