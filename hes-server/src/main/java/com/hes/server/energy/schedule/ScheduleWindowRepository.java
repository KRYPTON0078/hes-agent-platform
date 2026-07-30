package com.hes.server.energy.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ScheduleWindowRepository extends JpaRepository<ScheduleWindowEntity, Long> {
    List<ScheduleWindowEntity> findByScheduleIdOrderByPriorityAsc(Long scheduleId);
}
