package com.hes.server.domain.device;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DeviceCredentialRepository extends JpaRepository<DeviceCredentialEntity, Long> {
    Optional<DeviceCredentialEntity> findByDevice_Id(Long deviceId);
}
