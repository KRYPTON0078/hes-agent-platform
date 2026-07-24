package com.hes.server.domain.device;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface DeviceRepository extends JpaRepository<DeviceEntity, Long> {
    Optional<DeviceEntity> findByDeviceId(String deviceId);
    List<DeviceEntity> findByStatus(DeviceStatus status);
    long countByStatus(DeviceStatus status);
}
