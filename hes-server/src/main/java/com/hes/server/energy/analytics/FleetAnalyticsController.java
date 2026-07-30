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