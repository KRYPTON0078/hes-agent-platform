package com.hes.server.ota;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface OtaJobRepository extends JpaRepository<OtaJobEntity, Long> {
    Optional<OtaJobEntity> findByJobCode(String jobCode);
    List<OtaJobEntity> findByDeviceIdOrderByUpdatedAtDesc(String deviceId);
}