package com.testimx.qaquiz.dto;

/**
 * Summary of a single answered question within a completed quiz
 * session.  Includes the question index (or ID) and whether the
 * user answered correctly.
 */
public class QuizItemResultDto {
    private Long questionId;
    private boolean correct;

    public QuizItemResultDto(Long questionId, boolean correct) {
        this.questionId = questionId;
        this.correct = correct;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public boolean isCorrect() {
        return correct;
    }
}