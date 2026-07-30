package com.hes.server.energy.analytics;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.Instant;
import java.util.List;

public interface EnergyForecastRepository extends JpaRepository<EnergyForecastEntity, Long> {
    List<EnergyForecastEntity> findByDeviceIdAndForecastHourBetweenOrderByForecastHourAsc(String deviceId, Instant from, Instant to);
}