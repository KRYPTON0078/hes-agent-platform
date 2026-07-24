package com.hes.server.domain.telemetry;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TelemetryLatestRepository extends JpaRepository<TelemetryLatestEntity, Long> {
}
