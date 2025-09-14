package com.testimx.qaquiz.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Category groups a number of questions under a common theme such as
 * AI Testing, ISTQB, Java or Selenium.  Each category keeps track of
 * the total number of questions for easier display on the client.
 */
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 512)
    private String description;

    /**
     * Optional icon identifier for UI purposes.
     */
    private String icon;

    /**
     * Number of questions contained in this category.  This field
     * serves as a denormalised count to avoid expensive counts on
     * large tables.  It should be kept in sync by the service layer
     * whenever questions are created or deleted.
     */
    @Column(name = "items_count")
    private Integer itemsCount = 0;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    public Category() {
    }

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getItemsCount() {
        return itemsCount;
    }

    public void setItemsCount(Integer itemsCount) {
        this.itemsCount = itemsCount;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}