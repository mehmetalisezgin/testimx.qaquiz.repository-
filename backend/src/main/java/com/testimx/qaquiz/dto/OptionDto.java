package com.testimx.qaquiz.dto;

/**
 * Simple representation of an answer option for transfer to the
 * client.  Contains the label and the display text; does not
 * expose whether the option is correct.
 */
public class OptionDto {
    private String label;
    private String text;

    public OptionDto(String label, String text) {
        this.label = label;
        this.text = text;
    }

    public String getLabel() {
        return label;
    }

    public String getText() {
        return text;
    }
}