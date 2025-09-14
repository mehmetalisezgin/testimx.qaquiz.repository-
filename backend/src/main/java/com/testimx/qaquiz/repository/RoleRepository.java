package com.testimx.qaquiz.repository;

import com.testimx.qaquiz.model.ERole;
import com.testimx.qaquiz.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for accessing role entities.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}