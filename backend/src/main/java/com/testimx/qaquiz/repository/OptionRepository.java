package com.testimx.qaquiz.repository;

import com.testimx.qaquiz.model.Option;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for answer options.
 */
public interface OptionRepository extends JpaRepository<Option, Long> {
}