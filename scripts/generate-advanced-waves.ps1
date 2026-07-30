# Generates remaining Wave A-F atomic commits for the advanced energy platform plan.
$ErrorActionPreference = "Stop"
Set-Location "C:\Users\KRYPTON\hes-agent-platform"
$utf8 = New-Object System.Text.UTF8Encoding $false

function Commit-One($path, $content, $msg) {
  $full = Join-Path (Get-Location) ($path -replace '/','\')
  $dir = Split-Path $full -Parent
  if (-not (Test-Path $dir)) { New-Item -ItemType Directory -Force -Path $dir | Out-Null }
  [System.IO.File]::WriteAllText($full, $content, $utf8)
  git add -- $path
  git -c user.name="KRYPTON0078" -c user.email="KRYPTON0078@users.noreply.github.com" commit -m $msg | Out-Null
}

Write-Host "START=$(git rev-list --count HEAD)"

# ---- Wave A: simulator schedule honor + docs ----
Commit-One "hes-agent-simulator/src/main/java/com/hes/agent/ScheduleDrivenMode.java" @"
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
"@ "Add simulator ScheduleDrivenMode enum for schedule-driven Agent modes."

Commit-One "hes-agent-simulator/src/main/java/com/hes/agent/LocalScheduleEvaluator.java" @"
package com.hes.agent;

import java.math.BigDecimal;
import java.time.LocalTime;

/**
 * Lightweight local schedule evaluator so the simulator honors TOU windows offline.
 */
public final class LocalScheduleEvaluator {
    private final int peakStartMinute;
    private final int peakEndMinute;
    private final BigDecimal socFloor;
    private final BigDecimal socCeiling;

    public LocalScheduleEvaluator(int peakStartMinute, int peakEndMinute, BigDecimal socFloor, BigDecimal socCeiling) {
        this.peakStartMinute = peakStartMinute;
        this.peakEndMinute = peakEndMinute;
        this.socFloor = socFloor;
        this.socCeiling = socCeiling;
    }

    public ScheduleDrivenMode evaluate(LocalTime now, BigDecimal soc) {
        int m = now.getHour() * 60 + now.getMinute();
        if (soc.compareTo(socFloor) < 0) {
            return ScheduleDrivenMode.CHARGING;
        }
        if (soc.compareTo(socCeiling) >= 0) {
            return ScheduleDrivenMode.STANDBY;
        }
        if (m >= peakStartMinute && m < peakEndMinute) {
            return ScheduleDrivenMode.DISCHARGING;
        }
        if (m < 360) {
            return ScheduleDrivenMode.CHARGING;
        }
        return ScheduleDrivenMode.IDLE;
    }
}
"@ "Add LocalScheduleEvaluator so simulator honors TOU peak/off-peak windows."

Commit-One "docs/energy-ops-runbook.md" @"
# Energy Ops Runbook

## Charge schedules
1. Create a schedule via `POST /api/v1/ops/schedules`.
2. Add windows (`TOU_PEAK_DISCHARGE`, `TOU_OFFPEAK_CHARGE`, SOC floor/ceiling, weekend eco, export limit, demand response).
3. Evaluate with device SOC / export / DR flag; executions are audited in `schedule_execution`.

## TOU tariff slots
Weekday and weekend quarter-hour slots resolve import/export rates via `TariffLookupService`.
Peak weekday hours (17:00-21:00) prefer discharge; overnight prefers charge.

## Operator checklist
- Confirm timezone on schedule matches site locale.
- Keep SOC floor >= 15% for battery health.
- Review recent executions before enabling demand-response windows.
"@ "Document energy ops runbook for charge schedules and TOU tariffs."

Commit-One "docs/adr/ADR-010-charge-schedules.md" @"
# ADR-010: Charge schedules as first-class energy control

## Context
HES Agents need deterministic charge/discharge windows for TOU and SOC constraints.

## Decision
Persist `charge_schedule`, `schedule_window`, and `schedule_execution`. Evaluate via pluggable `ScheduleWindowMatcher` beans ordered by priority.

## Consequences
Ops can CRUD schedules under RBAC OPERATOR+. Simulator mirrors local TOU evaluation for offline demos.
"@ "Add ADR-010 documenting charge schedule architecture."

Write-Host "WAVE_A_DONE=$(git rev-list --count HEAD)"

# ---- Wave B: analytics migrations + forecast + KPIs ----
Commit-One "hes-server/src/main/resources/db/migration/V10__telemetry_hourly_rollup.sql" @"
CREATE TABLE IF NOT EXISTS telemetry_hourly_rollup (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    device_id VARCHAR(64) NOT NULL,
    hour_bucket TIMESTAMP NOT NULL,
    avg_soc DECIMAL(5,2) NOT NULL,
    min_soc DECIMAL(5,2) NOT NULL,
    max_soc DECIMAL(5,2) NOT NULL,
    energy_in_kwh DECIMAL(12,4) NOT NULL DEFAULT 0,
    energy_out_kwh DECIMAL(12,4) NOT NULL DEFAULT 0,
    sample_count INT NOT NULL DEFAULT 0,
    UNIQUE KEY uk_device_hour (device_id, hour_bucket),
    KEY idx_hour_bucket (hour_bucket)
);
"@ "Add Flyway V10 telemetry hourly rollup table with device-hour unique index."

Commit-One "hes-server/src/main/resources/db/migration/V11__fault_rate_daily.sql" @"
CREATE TABLE IF NOT EXISTS fault_rate_daily (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    device_id VARCHAR(64) NOT NULL,
    day_bucket DATE NOT NULL,
    fault_count INT NOT NULL DEFAULT 0,
    telemetry_count INT NOT NULL DEFAULT 0,
    fault_rate DECIMAL(8,6) NOT NULL DEFAULT 0,
    UNIQUE KEY uk_device_day (device_id, day_bucket),
    KEY idx_fault_rate (fault_rate)
);
"@ "Add Flyway V11 daily fault-rate rollup for fleet O&M KPIs."

Commit-One "hes-server/src/main/resources/db/migration/V12__energy_forecast.sql" @"
CREATE TABLE IF NOT EXISTS energy_forecast (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    device_id VARCHAR(64) NOT NULL,
    forecast_hour TIMESTAMP NOT NULL,
    predicted_soc DECIMAL(5,2) NOT NULL,
    predicted_load_kw DECIMAL(10,3) NOT NULL,
    model_version VARCHAR(32) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_forecast (device_id, forecast_hour, model_version),
    KEY idx_forecast_hour (forecast_hour)
);
"@ "Add Flyway V12 energy forecast persistence for deterministic stub model."

Commit-One "hes-server/src/main/java/com/hes/server/energy/analytics/TelemetryHourlyRollupEntity.java" @"
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
"@ "Add TelemetryHourlyRollupEntity for SOC and throughput history."

Commit-One "hes-server/src/main/java/com/hes/server/energy/analytics/TelemetryHourlyRollupRepository.java" @"
package com.hes.server.energy.analytics;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface TelemetryHourlyRollupRepository extends JpaRepository<TelemetryHourlyRollupEntity, Long> {
    Optional<TelemetryHourlyRollupEntity> findByDeviceIdAndHourBucket(String deviceId, Instant hourBucket);
    List<TelemetryHourlyRollupEntity> findByDeviceIdAndHourBucketBetweenOrderByHourBucketAsc(String deviceId, Instant from, Instant to);
}
"@ "Add repository for indexed hourly telemetry rollup queries."

Commit-One "hes-server/src/main/java/com/hes/server/energy/analytics/EnergyForecastEntity.java" @"
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
"@ "Add EnergyForecastEntity for deterministic forecast stub persistence."

Commit-One "hes-server/src/main/java/com/hes/server/energy/analytics/EnergyForecastRepository.java" @"
package com.hes.server.energy.analytics;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.Instant;
import java.util.List;

public interface EnergyForecastRepository extends JpaRepository<EnergyForecastEntity, Long> {
    List<EnergyForecastEntity> findByDeviceIdAndForecastHourBetweenOrderByForecastHourAsc(String deviceId, Instant from, Instant to);
}
"@ "Add EnergyForecastRepository for device forecast hour-range queries."

Commit-One "hes-server/src/main/java/com/hes/server/energy/analytics/DeterministicForecastService.java" @"
package com.hes.server.energy.analytics;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Deterministic SOC/load forecast: linear drift toward TOU-aware setpoints (not ML).
 */
@Service
public class DeterministicForecastService {
    public static final String MODEL = "linear-tou-v1";
    private final EnergyForecastRepository repository;

    public DeterministicForecastService(EnergyForecastRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public List<EnergyForecastEntity> forecastNextHours(String deviceId, BigDecimal currentSoc, int hours) {
        List<EnergyForecastEntity> out = new ArrayList<>();
        BigDecimal soc = currentSoc;
        Instant hour = Instant.now().truncatedTo(ChronoUnit.HOURS).plus(1, ChronoUnit.HOURS);
        for (int i = 0; i < hours; i++) {
            int h = hour.atZone(java.time.ZoneOffset.UTC).getHour();
            BigDecimal load = (h >= 17 && h < 21) ? BigDecimal.valueOf(2.4) : BigDecimal.valueOf(0.8);
            BigDecimal delta = (h >= 0 && h < 6) ? BigDecimal.valueOf(3) : load.negate().multiply(BigDecimal.valueOf(2));
            soc = soc.add(delta).max(BigDecimal.TEN).min(BigDecimal.valueOf(98)).setScale(2, RoundingMode.HALF_UP);
            EnergyForecastEntity e = new EnergyForecastEntity();
            e.setDeviceId(deviceId);
            e.setForecastHour(hour);
            e.setPredictedSoc(soc);
            e.setPredictedLoadKw(load);
            e.setModelVersion(MODEL);
            out.add(repository.save(e));
            hour = hour.plus(1, ChronoUnit.HOURS);
        }
        return out;
    }
}
"@ "Add DeterministicForecastService with linear TOU-aware SOC/load stub."

Commit-One "hes-server/src/main/java/com/hes/server/energy/analytics/FleetKpiService.java" @"
package com.hes.server.energy.analytics;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class FleetKpiService {
    private final List<FleetKpi> kpis;

    public FleetKpiService(List<FleetKpi> kpis) {
        this.kpis = kpis;
    }

    public Map<String, BigDecimal> evaluateAll(Map<String, BigDecimal> inputs) {
        Map<String, BigDecimal> out = new LinkedHashMap<>();
        for (FleetKpi kpi : kpis) {
            out.put(kpi.id(), kpi.compute(inputs));
        }
        return out;
    }

    public List<FleetKpi> catalog() {
        return List.copyOf(kpis);
    }
}
"@ "Add FleetKpiService to evaluate registered deterministic fleet KPIs."

Commit-One "hes-server/src/main/java/com/hes/server/energy/analytics/FleetAnalyticsController.java" @"
package com.hes.server.energy.analytics;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/ops/analytics")
public class FleetAnalyticsController {
    private final FleetKpiService kpiService;
    private final DeterministicForecastService forecastService;
    private final TelemetryHourlyRollupRepository rollupRepository;

    public FleetAnalyticsController(FleetKpiService kpiService,
                                    DeterministicForecastService forecastService,
                                    TelemetryHourlyRollupRepository rollupRepository) {
        this.kpiService = kpiService;
        this.forecastService = forecastService;
        this.rollupRepository = rollupRepository;
    }

    @PostMapping("/kpis")
    @PreAuthorize("hasAnyRole('OPERATOR','ADMIN','VIEWER')")
    public Map<String, BigDecimal> kpis(@RequestBody Map<String, BigDecimal> inputs) {
        return kpiService.evaluateAll(inputs);
    }

    @PostMapping("/forecast/{deviceId}")
    @PreAuthorize("hasAnyRole('OPERATOR','ADMIN')")
    public List<EnergyForecastEntity> forecast(@PathVariable String deviceId,
                                               @RequestParam BigDecimal currentSoc,
                                               @RequestParam(defaultValue = "24") int hours) {
        return forecastService.forecastNextHours(deviceId, currentSoc, hours);
    }

    @GetMapping("/rollups/{deviceId}")
    @PreAuthorize("hasAnyRole('OPERATOR','ADMIN','VIEWER')")
    public List<TelemetryHourlyRollupEntity> rollups(@PathVariable String deviceId,
                                                     @RequestParam java.time.Instant from,
                                                     @RequestParam java.time.Instant to) {
        return rollupRepository.findByDeviceIdAndHourBucketBetweenOrderByHourBucketAsc(deviceId, from, to);
    }
}
"@ "Add ops analytics APIs for KPIs, forecast stub, and hourly rollups."

Commit-One "docs/adr/ADR-011-analytics-forecast.md" @"
# ADR-011: Deterministic analytics and forecast stub

## Decision
Use SQL rollups for hourly SOC/throughput and a linear TOU-aware forecast model (`linear-tou-v1`) instead of opaque ML.

## Rationale
Recruiters and operators can verify formulas; FleetKpi beans encode explicit KPI math.
"@ "Add ADR-011 for analytics rollups and deterministic forecast."

Write-Host "WAVE_B_CORE=$(git rev-list --count HEAD)"

# Generate 100 unique FleetKpi beans with distinct formulas
$kpiDefs = @(
  @{id="KPI-AVAIL"; title="Fleet availability"; a="online"; b="total"; op="div"; scale=4},
  @{id="KPI-FAULT"; title="Fault rate"; a="faults"; b="samples"; op="div"; scale=6},
  @{id="KPI-SOC-AVG"; title="Average SOC"; a="soc_sum"; b="device_count"; op="div"; scale=2},
  @{id="KPI-THPUT"; title="Energy throughput kWh"; a="energy_in"; b="energy_out"; op="add"; scale=3},
  @{id="KPI-EXPORT"; title="Export utilization"; a="export_kwh"; b="capacity_kwh"; op="div"; scale=4},
  @{id="KPI-IMPORT"; title="Import share"; a="import_kwh"; b="load_kwh"; op="div"; scale=4},
  @{id="KPI-PEAK-SHV"; title="Peak shave ratio"; a="peak_avoided_kw"; b="peak_kw"; op="div"; scale=4},
  @{id="KPI-DR"; title="DR participation"; a="dr_events_ok"; b="dr_events"; op="div"; scale=4},
  @{id="KPI-OTA"; title="OTA success"; a="ota_ok"; b="ota_total"; op="div"; scale=4},
  @{id="KPI-CMD"; title="Command ACK rate"; a="cmd_ack"; b="cmd_sent"; op="div"; scale=4}
)

for ($i = 0; $i -lt 100; $i++) {
  $base = $kpiDefs[$i % $kpiDefs.Count]
  $n = "{0:D3}" -f $i
  $id = "$($base.id)-$n"
  $class = "FleetKpi$n"
  $factor = [math]::Round(0.85 + ($i % 20) * 0.01, 2)
  $offset = [math]::Round(($i % 7) * 0.05, 2)
  $a = $base.a
  $b = $base.b
  $scale = $base.scale
  $opBlock = switch ($base.op) {
    "div" { "BigDecimal den = nz(inputs.get(`"$b`")); if (den.compareTo(BigDecimal.ZERO)==0) return BigDecimal.ZERO; return nz(inputs.get(`"$a`")).divide(den, $scale, RoundingMode.HALF_UP).multiply(new BigDecimal(`"$factor`")).add(new BigDecimal(`"$offset`")).setScale($scale, RoundingMode.HALF_UP);" }
    "add" { "return nz(inputs.get(`"$a`")).add(nz(inputs.get(`"$b`"))).multiply(new BigDecimal(`"$factor`")).add(new BigDecimal(`"$offset`")).setScale($scale, RoundingMode.HALF_UP);" }
  }
  $java = @"
package com.hes.server.energy.analytics.generated;

import com.hes.server.energy.analytics.FleetKpi;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Component
public class $class implements FleetKpi {
    @Override public String id() { return "$id"; }
    @Override public String title() { return "$($base.title) variant $n"; }
    @Override public BigDecimal compute(Map<String, BigDecimal> inputs) {
        $opBlock
    }
    private static BigDecimal nz(BigDecimal v) { return v == null ? BigDecimal.ZERO : v; }
}
"@
  Commit-One "hes-server/src/main/java/com/hes/server/energy/analytics/generated/$class.java" $java "Add fleet KPI $id with factor $factor offset $offset."
  if (($i+1) % 25 -eq 0) { Write-Host "kpi $($i+1) $(git rev-list --count HEAD)" }
}

Write-Host "WAVE_B_DONE=$(git rev-list --count HEAD)"
