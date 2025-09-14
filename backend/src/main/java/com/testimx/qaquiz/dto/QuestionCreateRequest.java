package com.testimx.qaquiz.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * Payload used by administrators to create or update a question.  A
 * valid request must include the text of the question, the ID of
 * the category it belongs to and a list of exactly four answer
 * options.  One and only one of those options must be marked
 * correct.
 */
public class QuestionCreateRequest {

    @NotBlank
    private String text;

    @NotNull
    private Long categoryId;

    @Valid
    @Size(min = 2, max = 10) // allow flexibility but encourage four options
    private List<OptionCreateRequest> options;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public List<OptionCreateRequest> getOptions() {
        return options;
    }

    public void setOptions(List<OptionCreateRequest> options) {
        this.options = options;
    }
}