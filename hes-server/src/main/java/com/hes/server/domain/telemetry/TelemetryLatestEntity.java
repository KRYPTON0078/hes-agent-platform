package com.hes.server.domain.telemetry;

import com.hes.server.domain.device.DeviceEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "telemetry_latest")
public class TelemetryLatestEntity {

    @Id
    @Column(name = "device_id")
    private Long deviceId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "device_id")
    private DeviceEntity device;

    @Column(name = "soc_percent", precision = 5, scale = 2)
    private BigDecimal socPercent;

    @Column(name = "battery_kwh", precision = 10, scale = 3)
    private BigDecimal batteryKwh;

    @Column(name = "inverter_watts", precision = 12, scale = 2)
    private BigDecimal inverterWatts;

    @Column(name = "grid_watts", precision = 12, scale = 2)
    private BigDecimal gridWatts;

    @Column(name = "home_load_watts", precision = 12, scale = 2)
    private BigDecimal homeLoadWatts;

    @Column(name = "battery_voltage", precision = 10, scale = 3)
    private BigDecimal batteryVoltage;

    @Column(name = "battery_current", precision = 10, scale = 3)
    private BigDecimal batteryCurrent;

    @Column(name = "fault_code")
    private Integer faultCode;

    @Column(name = "fault_message", length = 256)
    private String faultMessage;

    @Column(name = "operating_mode", length = 32)
    private String operatingMode;

    @Column(name = "reported_at", nullable = false)
    private Instant reportedAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    @PreUpdate
    void touch() {
        updatedAt = Instant.now();
    }

    public Long getDeviceId() { return deviceId; }
    public DeviceEntity getDevice() { return device; }
    public void setDevice(DeviceEntity device) { this.device = device; }
    public BigDecimal getSocPercent() { return socPercent; }
    public void setSocPercent(BigDecimal socPercent) { this.socPercent = socPercent; }
    public BigDecimal getBatteryKwh() { return batteryKwh; }
    public void setBatteryKwh(BigDecimal batteryKwh) { this.batteryKwh = batteryKwh; }
    public BigDecimal getInverterWatts() { return inverterWatts; }
    public void setInverterWatts(BigDecimal inverterWatts) { this.inverterWatts = inverterWatts; }
    public BigDecimal getGridWatts() { return gridWatts; }
    public void setGridWatts(BigDecimal gridWatts) { this.gridWatts = gridWatts; }
    public BigDecimal getHomeLoadWatts() { return homeLoadWatts; }
    public void setHomeLoadWatts(BigDecimal homeLoadWatts) { this.homeLoadWatts = homeLoadWatts; }
    public BigDecimal getBatteryVoltage() { return batteryVoltage; }
    public void setBatteryVoltage(BigDecimal batteryVoltage) { this.batteryVoltage = batteryVoltage; }
    public BigDecimal getBatteryCurrent() { return batteryCurrent; }
    public void setBatteryCurrent(BigDecimal batteryCurrent) { this.batteryCurrent = batteryCurrent; }
    public Integer getFaultCode() { return faultCode; }
    public void setFaultCode(Integer faultCode) { this.faultCode = faultCode; }
    public String getFaultMessage() { return faultMessage; }
    public void setFaultMessage(String faultMessage) { this.faultMessage = faultMessage; }
    public String getOperatingMode() { return operatingMode; }
    public void setOperatingMode(String operatingMode) { this.operatingMode = operatingMode; }
    public Instant getReportedAt() { return reportedAt; }
    public void setReportedAt(Instant reportedAt) { this.reportedAt = reportedAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
