package com.testimx.qaquiz.repository;

import com.testimx.qaquiz.model.Category;
import com.testimx.qaquiz.model.QuizSession;
import com.testimx.qaquiz.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for quiz sessions.
 */
public interface QuizSessionRepository extends JpaRepository<QuizSession, Long> {
    List<QuizSession> findByUser(User user);
    List<QuizSession> findByUserAndCategory(User user, Category category);
}