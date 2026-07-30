package com.hes.server.energy.analytics;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "telemetry_hourly_rollup")
public class TelemetryHourlyRollupEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "device_id", nullable = false, length = 64) private String deviceId;
    @Column(name = "hour_bucket", nullable = false) private Instant hourBucket;
    @Column(name = "avg_soc", nullable = false, precision = 5, scale = 2) private BigDecimal avgSoc;
    @Column(name = "min_soc", nullable = false, precision = 5, scale = 2) private BigDecimal minSoc;
    @Column(name = "max_soc", nullable = false, precision = 5, scale = 2) private BigDecimal maxSoc;
    @Column(name = "energy_in_kwh", nullable = false, precision = 12, scale = 4) private BigDecimal energyInKwh = BigDecimal.ZERO;
    @Column(name = "energy_out_kwh", nullable = false, precision = 12, scale = 4) private BigDecimal energyOutKwh = BigDecimal.ZERO;
    @Column(name = "sample_count", nullable = false) private int sampleCount;

    public Long getId() { return id; }
    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
    public Instant getHourBucket() { return hourBucket; }
    public void setHourBucket(Instant hourBucket) { this.hourBucket = hourBucket; }
    public BigDecimal getAvgSoc() { return avgSoc; }
    public void setAvgSoc(BigDecimal avgSoc) { this.avgSoc = avgSoc; }
    public BigDecimal getMinSoc() { return minSoc; }
    public void setMinSoc(BigDecimal minSoc) { this.minSoc = minSoc; }
    public BigDecimal getMaxSoc() { return maxSoc; }
    public void setMaxSoc(BigDecimal maxSoc) { this.maxSoc = maxSoc; }
    public BigDecimal getEnergyInKwh() { return energyInKwh; }
    public void setEnergyInKwh(BigDecimal energyInKwh) { this.energyInKwh = energyInKwh; }
    public BigDecimal getEnergyOutKwh() { return energyOutKwh; }
    public void setEnergyOutKwh(BigDecimal energyOutKwh) { this.energyOutKwh = energyOutKwh; }
    public int getSampleCount() { return sampleCount; }
    public void setSampleCount(int sampleCount) { this.sampleCount = sampleCount; }
}