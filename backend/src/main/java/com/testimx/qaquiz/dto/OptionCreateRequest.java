package com.testimx.qaquiz.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Payload used when creating or updating an answer option.  Admins
 * must specify the label (e.g. "A") and text.  For exactly one
 * option per question {@code correct} must be true.
 */
public class OptionCreateRequest {
    @NotBlank
    private String label;
    @NotBlank
    private String text;
    @NotNull
    private Boolean correct;
    private String explanation;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getCorrect() {
        return correct;
    }

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}