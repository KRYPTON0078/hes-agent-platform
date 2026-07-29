package com.hes.server.security.audit;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SecurityAuditEventRepository extends JpaRepository<SecurityAuditEventEntity, Long> {
    List<SecurityAuditEventEntity> findTop100ByOrderByCreatedAtDesc();
    List<SecurityAuditEventEntity> findTop100ByEventTypeOrderByCreatedAtDesc(String eventType);
}
