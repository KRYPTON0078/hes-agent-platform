package com.hes.server.energy.analytics;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface TelemetryHourlyRollupRepository extends JpaRepository<TelemetryHourlyRollupEntity, Long> {
    Optional<TelemetryHourlyRollupEntity> findByDeviceIdAndHourBucket(String deviceId, Instant hourBucket);
    List<TelemetryHourlyRollupEntity> findByDeviceIdAndHourBucketBetweenOrderByHourBucketAsc(String deviceId, Instant from, Instant to);
}