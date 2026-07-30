package com.hes.server.energy.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ChargeScheduleRepository extends JpaRepository<ChargeScheduleEntity, Long> {
    Optional<ChargeScheduleEntity> findByScheduleCode(String scheduleCode);
    List<ChargeScheduleEntity> findByDeviceIdAndEnabledTrue(String deviceId);
}
