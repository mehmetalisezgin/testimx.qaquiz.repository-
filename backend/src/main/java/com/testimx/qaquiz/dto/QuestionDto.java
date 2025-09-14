package com.testimx.qaquiz.dto;

import java.util.List;

/**
 * Representation of a quiz question to send to the client.  Contains
 * the question text and a list of answer options without revealing
 * which option is correct.
 */
public class QuestionDto {
    private Long id;
    private String text;
    private Long categoryId;
    private List<OptionDto> options;

    public QuestionDto(Long id, String text, Long categoryId, List<OptionDto> options) {
        this.id = id;
        this.text = text;
        this.categoryId = categoryId;
        this.options = options;
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public List<OptionDto> getOptions() {
        return options;
    }
}