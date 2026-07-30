package com.hes.server.energy.dispatch;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DispatchEventRepository extends JpaRepository<DispatchEventEntity, Long> {
    List<DispatchEventEntity> findByPublishedFalseOrderByCreatedAtAsc();
}