package com.testimx.qaquiz.dto;

import java.util.List;

/**
 * Aggregated progress response summarising the user's performance
 * across all quiz sessions.  Contains overall correctness,
 * streak length, number of quizzes taken, per‑category progress
 * and a history of recent sessions.
 */
public class ProgressSummaryResponse {
    private double overallCorrectPercent;
    private int streak;
    private int totalQuizzes;
    private List<CategoryProgressDto> categories;
    private List<QuizHistoryDto> history;

    public ProgressSummaryResponse(double overallCorrectPercent, int streak, int totalQuizzes,
                                   List<CategoryProgressDto> categories, List<QuizHistoryDto> history) {
        this.overallCorrectPercent = overallCorrectPercent;
        this.streak = streak;
        this.totalQuizzes = totalQuizzes;
        this.categories = categories;
        this.history = history;
    }

    public double getOverallCorrectPercent() {
        return overallCorrectPercent;
    }

    public int getStreak() {
        return streak;
    }

    public int getTotalQuizzes() {
        return totalQuizzes;
    }

    public List<CategoryProgressDto> getCategories() {
        return categories;
    }

    public List<QuizHistoryDto> getHistory() {
        return history;
    }
}