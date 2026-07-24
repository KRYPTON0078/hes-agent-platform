package com.hes.server.domain.alert;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AlertRepository extends JpaRepository<AlertEntity, Long> {
    List<AlertEntity> findByStatusOrderByOpenedAtDesc(AlertStatus status);
    Optional<AlertEntity> findFirstByDevice_IdAndAlertTypeAndStatus(Long deviceId, String alertType, AlertStatus status);
    long countByStatus(AlertStatus status);
}
