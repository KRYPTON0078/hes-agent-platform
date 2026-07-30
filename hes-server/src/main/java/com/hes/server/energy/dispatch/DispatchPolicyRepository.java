package com.hes.server.energy.dispatch;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface DispatchPolicyRepository extends JpaRepository<DispatchPolicyEntity, Long> {
    Optional<DispatchPolicyEntity> findByPolicyCode(String policyCode);
    List<DispatchPolicyEntity> findByEnabledTrueOrderByPriorityAsc();
}