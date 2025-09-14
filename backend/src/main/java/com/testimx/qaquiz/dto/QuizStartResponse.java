package com.testimx.qaquiz.dto;

import java.util.List;

/**
 * Response sent when a new quiz session is started.  Contains the
 * session ID and a list of questions with their options.  The
 * client must include the session ID when submitting answers.
 */
public class QuizStartResponse {
    private Long sessionId;
    private List<QuestionDto> questions;

    public QuizStartResponse(Long sessionId, List<QuestionDto> questions) {
        this.sessionId = sessionId;
        this.questions = questions;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public List<QuestionDto> getQuestions() {
        return questions;
    }
}