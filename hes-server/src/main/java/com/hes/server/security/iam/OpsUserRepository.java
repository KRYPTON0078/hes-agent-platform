package com.hes.server.security.iam;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OpsUserRepository extends JpaRepository<OpsUserEntity, Long> {
    Optional<OpsUserEntity> findByUsername(String username);
    boolean existsByUsername(String username);
}
