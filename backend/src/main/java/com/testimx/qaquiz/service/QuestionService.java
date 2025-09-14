package com.testimx.qaquiz.service;

import com.testimx.qaquiz.dto.OptionCreateRequest;
import com.testimx.qaquiz.dto.OptionDto;
import com.testimx.qaquiz.dto.QuestionCreateRequest;
import com.testimx.qaquiz.dto.QuestionDto;
import com.testimx.qaquiz.model.Category;
import com.testimx.qaquiz.model.Option;
import com.testimx.qaquiz.model.Question;
import com.testimx.qaquiz.repository.CategoryRepository;
import com.testimx.qaquiz.repository.OptionRepository;
import com.testimx.qaquiz.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service encapsulating question management logic.
 */
@Service
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private OptionRepository optionRepository;

    /**
     * Create a new question along with its options.  Ensures that
     * exactly one option is marked as correct.
     */
    @Transactional
    public QuestionDto createQuestion(QuestionCreateRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        // Validate that exactly one option is correct
        long correctCount = request.getOptions().stream().filter(OptionCreateRequest::getCorrect).count();
        if (correctCount != 1) {
            throw new IllegalArgumentException("Exactly one option must be marked as correct");
        }
        Question question = new Question();
        question.setText(request.getText());
        question.setCategory(category);
        List<Option> optionEntities = new ArrayList<>();
        for (OptionCreateRequest optReq : request.getOptions()) {
            Option opt = new Option();
            opt.setLabel(optReq.getLabel());
            opt.setText(optReq.getText());
            opt.setCorrect(Boolean.TRUE.equals(optReq.getCorrect()));
            opt.setExplanation(optReq.getExplanation());
            opt.setQuestion(question);
            optionEntities.add(opt);
        }
        question.setOptions(optionEntities);
        Question saved = questionRepository.save(question);
        // Update items count on category
        category.setItemsCount(category.getItemsCount() + 1);
        categoryRepository.save(category);
        return toDto(saved);
    }

    /**
     * Update an existing question.  Replaces text and options.
     */
    @Transactional
    public QuestionDto updateQuestion(Long questionId, QuestionCreateRequest request) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));
        if (!question.getCategory().getId().equals(request.getCategoryId())) {
            // Category change is not supported for simplicity
            throw new IllegalArgumentException("Changing the category of a question is not supported");
        }
        long correctCount = request.getOptions().stream().filter(OptionCreateRequest::getCorrect).count();
        if (correctCount != 1) {
            throw new IllegalArgumentException("Exactly one option must be marked as correct");
        }
        question.setText(request.getText());
        // Remove existing options and create new ones
        question.getOptions().clear();
        for (OptionCreateRequest optReq : request.getOptions()) {
            Option opt = new Option();
            opt.setLabel(optReq.getLabel());
            opt.setText(optReq.getText());
            opt.setCorrect(Boolean.TRUE.equals(optReq.getCorrect()));
            opt.setExplanation(optReq.getExplanation());
            opt.setQuestion(question);
            question.getOptions().add(opt);
        }
        Question saved = questionRepository.save(question);
        return toDto(saved);
    }

    /**
     * Delete a question by id and decrement the category's item count.
     */
    @Transactional
    public void deleteQuestion(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));
        Category category = question.getCategory();
        questionRepository.delete(question);
        category.setItemsCount(Math.max(0, category.getItemsCount() - 1));
        categoryRepository.save(category);
    }

    /**
     * Retrieve questions by category.  Converts to DTOs for client use.
     */
    @Transactional(readOnly = true)
    public List<QuestionDto> getQuestionsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return questionRepository.findByCategory(category).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private QuestionDto toDto(Question question) {
        List<OptionDto> options = question.getOptions().stream()
                .map(opt -> new OptionDto(opt.getLabel(), opt.getText()))
                .collect(Collectors.toList());
        return new QuestionDto(question.getId(), question.getText(), question.getCategory().getId(), options);
    }
}