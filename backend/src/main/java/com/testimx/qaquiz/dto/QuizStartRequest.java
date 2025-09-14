package com.testimx.qaquiz.dto;

import jakarta.validation.constraints.NotNull;

/**
 * Request used when starting a new quiz.  Requires the category ID
 * and optionally the number of questions to include.  If
 * {@code numQuestions} is omitted or larger than the number of
 * available questions then all questions will be included.
 */
public class QuizStartRequest {
    @NotNull
    private Long categoryId;
    private Integer numQuestions;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getNumQuestions() {
        return numQuestions;
    }

    public void setNumQuestions(Integer numQuestions) {
        this.numQuestions = numQuestions;
    }
}