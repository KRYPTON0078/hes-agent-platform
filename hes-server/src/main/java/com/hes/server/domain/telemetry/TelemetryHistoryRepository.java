package com.hes.server.domain.telemetry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface TelemetryHistoryRepository extends JpaRepository<TelemetryHistoryEntity, Long> {

    @Query("""
            select t from TelemetryHistoryEntity t
            where t.device.deviceId = :deviceId
              and t.reportedAt >= :from
              and t.reportedAt < :to
            order by t.reportedAt asc
            """)
    List<TelemetryHistoryEntity> findRange(
            @Param("deviceId") String deviceId,
            @Param("from") Instant from,
            @Param("to") Instant to
    );
}
