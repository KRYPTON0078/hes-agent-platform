package com.hes.server.security.iam;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OpsRoleRepository extends JpaRepository<OpsRoleEntity, Long> {
    Optional<OpsRoleEntity> findByRoleCode(String roleCode);
}
