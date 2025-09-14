package com.testimx.qaquiz.repository;

import com.testimx.qaquiz.model.QuizSessionItem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for per‑question results.
 */
public interface QuizSessionItemRepository extends JpaRepository<QuizSessionItem, Long> {
}