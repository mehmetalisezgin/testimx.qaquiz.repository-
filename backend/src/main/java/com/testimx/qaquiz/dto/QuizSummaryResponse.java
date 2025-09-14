package com.testimx.qaquiz.dto;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Summary of a completed quiz session.  Contains the number of
 * correct answers, total number of questions, a percentage score,
 * the duration in seconds and a list of per‑question results.
 */
public class QuizSummaryResponse {
    private Long sessionId;
    private int correctCount;
    private int totalQuestions;
    private double percentage;
    private String duration;
    private List<QuizItemResultDto> results;

    public QuizSummaryResponse(Long sessionId, int correctCount, int totalQuestions, double percentage,
                               Duration duration, List<QuizItemResultDto> results) {
        this.sessionId = sessionId;
        this.correctCount = correctCount;
        this.totalQuestions = totalQuestions;
        this.percentage = percentage;
        // Format duration as m's's'' (e.g. "2m 30s").
        long seconds = duration.getSeconds();
        long minutesPart = seconds / 60;
        long secondsPart = seconds % 60;
        this.duration = String.format("%dm %ds", minutesPart, secondsPart);
        this.results = results;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public int getCorrectCount() {
        return correctCount;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public double getPercentage() {
        return percentage;
    }

    public String getDuration() {
        return duration;
    }

    public List<QuizItemResultDto> getResults() {
        return results;
    }
}