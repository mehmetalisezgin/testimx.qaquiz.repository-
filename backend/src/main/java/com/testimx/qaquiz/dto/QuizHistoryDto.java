package com.testimx.qaquiz.dto;

/**
 * Summary entry for a previously completed quiz, used in the
 * history list on the progress screen.  Contains the category
 * name, date and score.
 */
public class QuizHistoryDto {
    private Long sessionId;
    private String categoryName;
    private String date; // formatted date
    private String score; // e.g. "7/10"
    private boolean passed;

    public QuizHistoryDto(Long sessionId, String categoryName, String date, String score, boolean passed) {
        this.sessionId = sessionId;
        this.categoryName = categoryName;
        this.date = date;
        this.score = score;
        this.passed = passed;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getDate() {
        return date;
    }

    public String getScore() {
        return score;
    }

    public boolean isPassed() {
        return passed;
    }
}