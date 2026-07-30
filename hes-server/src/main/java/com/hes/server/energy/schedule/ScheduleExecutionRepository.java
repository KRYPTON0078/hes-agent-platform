package com.hes.server.energy.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ScheduleExecutionRepository extends JpaRepository<ScheduleExecutionEntity, Long> {
    List<ScheduleExecutionEntity> findTop50ByDeviceIdOrderByExecutedAtDesc(String deviceId);
}
