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