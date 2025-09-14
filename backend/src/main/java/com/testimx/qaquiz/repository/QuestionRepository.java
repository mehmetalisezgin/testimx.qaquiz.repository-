package com.testimx.qaquiz.repository;

import com.testimx.qaquiz.model.Category;
import com.testimx.qaquiz.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for questions.
 */
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByCategory(Category category);
}