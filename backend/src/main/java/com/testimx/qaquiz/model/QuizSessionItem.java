package com.testimx.qaquiz.model;

import jakarta.persistence.*;

/**
 * Represents the result of answering a single question within a
 * quiz session.  Stores which option the user selected and whether
 * it was correct.
 */
@Entity
@Table(name = "quiz_session_items")
public class QuizSessionItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "session_id")
    private QuizSession session;

    @ManyToOne(optional = false)
    @JoinColumn(name = "question_id")
    private Question question;

    /**
     * The option selected by the user.  We store the label rather
     * than the Option object itself to decouple the session from
     * future edits to the question.
     */
    @Column(length = 2)
    private String selectedLabel;

    /**
     * Flag indicating whether the answer was correct at the time of
     * answering.
     */
    private boolean correct;

    public QuizSessionItem() {
    }

    public QuizSessionItem(QuizSession session, Question question, String selectedLabel, boolean correct) {
        this.session = session;
        this.question = question;
        this.selectedLabel = selectedLabel;
        this.correct = correct;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public QuizSession getSession() {
        return session;
    }

    public void setSession(QuizSession session) {
        this.session = session;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getSelectedLabel() {
        return selectedLabel;
    }

    public void setSelectedLabel(String selectedLabel) {
        this.selectedLabel = selectedLabel;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}