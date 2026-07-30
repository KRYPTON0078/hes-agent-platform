package com.hes.server.energy.analytics;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "energy_forecast")
public class EnergyForecastEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "device_id", nullable = false, length = 64) private String deviceId;
    @Column(name = "forecast_hour", nullable = false) private Instant forecastHour;
    @Column(name = "predicted_soc", nullable = false, precision = 5, scale = 2) private BigDecimal predictedSoc;
    @Column(name = "predicted_load_kw", nullable = false, precision = 10, scale = 3) private BigDecimal predictedLoadKw;
    @Column(name = "model_version", nullable = false, length = 32) private String modelVersion;

    public Long getId() { return id; }
    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
    public Instant getForecastHour() { return forecastHour; }
    public void setForecastHour(Instant forecastHour) { this.forecastHour = forecastHour; }
    public BigDecimal getPredictedSoc() { return predictedSoc; }
    public void setPredictedSoc(BigDecimal predictedSoc) { this.predictedSoc = predictedSoc; }
    public BigDecimal getPredictedLoadKw() { return predictedLoadKw; }
    public void setPredictedLoadKw(BigDecimal predictedLoadKw) { this.predictedLoadKw = predictedLoadKw; }
    public String getModelVersion() { return modelVersion; }
    public void setModelVersion(String modelVersion) { this.modelVersion = modelVersion; }
}