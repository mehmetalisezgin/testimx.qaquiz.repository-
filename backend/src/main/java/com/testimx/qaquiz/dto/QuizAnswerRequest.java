package com.testimx.qaquiz.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Payload sent by the client when answering a single question during
 * a quiz session.  The session ID identifies the running quiz,
 * question ID denotes which question is being answered and
 * {@code selectedLabel} specifies the option chosen by the user.
 */
public class QuizAnswerRequest {
    @NotNull
    private Long sessionId;
    @NotNull
    private Long questionId;
    @NotBlank
    private String selectedLabel;

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getSelectedLabel() {
        return selectedLabel;
    }

    public void setSelectedLabel(String selectedLabel) {
        this.selectedLabel = selectedLabel;
    }
}