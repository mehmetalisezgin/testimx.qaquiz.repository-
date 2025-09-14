package com.testimx.qaquiz.dto;

/**
 * Response returned after answering a question.  Indicates whether
 * the answer was correct and provides an explanation for
 * incorrect answers.  The client may use this information to
 * present immediate feedback and highlight the selected option.
 */
public class QuizAnswerResponse {
    private boolean correct;
    private String explanation;

    public QuizAnswerResponse(boolean correct, String explanation) {
        this.correct = correct;
        this.explanation = explanation;
    }

    public boolean isCorrect() {
        return correct;
    }

    public String getExplanation() {
        return explanation;
    }
}