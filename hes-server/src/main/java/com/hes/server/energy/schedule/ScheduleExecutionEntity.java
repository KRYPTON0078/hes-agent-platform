package com.hes.server.energy.schedule;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "schedule_execution")
public class ScheduleExecutionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "schedule_id", nullable = false)
    private Long scheduleId;

    @Column(name = "window_id")
    private Long windowId;

    @Column(name = "device_id", nullable = false, length = 64)
    private String deviceId;

    @Column(name = "decided_mode", nullable = false, length = 32)
    private String decidedMode;

    @Column(nullable = false, length = 256)
    private String reason;

    @Column(name = "executed_at", nullable = false)
    private Instant executedAt;

    @PrePersist
    void onCreate() {
        executedAt = Instant.now();
    }

    public Long getId() { return id; }
    public Long getScheduleId() { return scheduleId; }
    public void setScheduleId(Long scheduleId) { this.scheduleId = scheduleId; }
    public Long getWindowId() { return windowId; }
    public void setWindowId(Long windowId) { this.windowId = windowId; }
    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
    public String getDecidedMode() { return decidedMode; }
    public void setDecidedMode(String decidedMode) { this.decidedMode = decidedMode; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public Instant getExecutedAt() { return executedAt; }
}
