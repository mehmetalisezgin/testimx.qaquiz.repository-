package com.testimx.qaquiz.dto;

/**
 * Aggregated statistics for a specific category, used on the
 * progress screen.  Indicates how many questions the user answered
 * correctly across all quizzes in that category.
 */
public class CategoryProgressDto {
    private Long categoryId;
    private String categoryName;
    private double percentCorrect;

    public CategoryProgressDto(Long categoryId, String categoryName, double percentCorrect) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.percentCorrect = percentCorrect;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public double getPercentCorrect() {
        return percentCorrect;
    }
}