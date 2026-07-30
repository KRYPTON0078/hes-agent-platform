package com.hes.server.energy.dispatch;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DispatchDecisionRepository extends JpaRepository<DispatchDecisionEntity, Long> {
    List<DispatchDecisionEntity> findTop50ByDeviceIdOrderByDecidedAtDesc(String deviceId);
}