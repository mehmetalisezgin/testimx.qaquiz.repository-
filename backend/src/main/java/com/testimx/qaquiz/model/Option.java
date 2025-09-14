package com.testimx.qaquiz.model;

import jakarta.persistence.*;

/**
 * Represents a possible answer for a question.  Each option has a
 * label (A–D), a human‑readable description, a boolean indicating
 * whether it is the correct answer, and an explanation that should
 * be sent to the user when they answer incorrectly.
 */
@Entity
@Table(name = "options")
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * A letter or identifier for the option (e.g. "A", "B", ...).
     */
    @Column(length = 2, nullable = false)
    private String label;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    /**
     * Flag indicating whether this option is the correct answer.
     */
    @Column(nullable = false)
    private boolean correct;

    /**
     * Explanation shown when a user selects an incorrect answer.
     * Stored in plain text/markdown so the client can format it.
     */
    @Column(columnDefinition = "TEXT")
    private String explanation;

    @ManyToOne(optional = false)
    @JoinColumn(name = "question_id")
    private Question question;

    public Option() {
    }

    public Option(String label, String text, boolean correct, String explanation) {
        this.label = label;
        this.text = text;
        this.correct = correct;
        this.explanation = explanation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}