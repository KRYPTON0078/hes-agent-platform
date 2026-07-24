package com.hes.server.domain.command;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommandEventRepository extends JpaRepository<CommandEventEntity, Long> {
    List<CommandEventEntity> findByCommandCommandIdOrderByCreatedAtAsc(String commandId);
}
