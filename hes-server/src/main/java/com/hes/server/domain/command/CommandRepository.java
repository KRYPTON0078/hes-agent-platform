package com.hes.server.domain.command;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface CommandRepository extends JpaRepository<CommandEntity, Long> {
    Optional<CommandEntity> findByCommandId(String commandId);
    Optional<CommandEntity> findByIdempotencyKey(String idempotencyKey);
    List<CommandEntity> findByStatusAndTimeoutAtBefore(CommandStatus status, Instant timeoutAt);
    List<CommandEntity> findByDeviceDeviceIdOrderByCreatedAtDesc(String deviceId);
}
