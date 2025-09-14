package com.testimx.qaquiz.service;

import com.testimx.qaquiz.dto.CategoryDto;
import com.testimx.qaquiz.dto.CategoryRequest;
import com.testimx.qaquiz.model.Category;
import com.testimx.qaquiz.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for categories.  Performs CRUD operations and
 * converts entities to DTOs.
 */
@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Retrieve all categories as DTOs.
     */
    @Transactional(readOnly = true)
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(cat -> new CategoryDto(cat.getId(), cat.getName(), cat.getDescription(), cat.getItemsCount(), cat.getIcon()))
                .collect(Collectors.toList());
    }

    /**
     * Create a new category.
     */
    public CategoryDto createCategory(CategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Category with the same name already exists");
        }
        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setIcon(request.getIcon());
        category.setItemsCount(0);
        Category saved = categoryRepository.save(category);
        return new CategoryDto(saved.getId(), saved.getName(), saved.getDescription(), saved.getItemsCount(), saved.getIcon());
    }

    /**
     * Update an existing category.
     */
    public CategoryDto updateCategory(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id=" + id));
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setIcon(request.getIcon());
        Category saved = categoryRepository.save(category);
        return new CategoryDto(saved.getId(), saved.getName(), saved.getDescription(), saved.getItemsCount(), saved.getIcon());
    }

    /**
     * Delete a category by id.
     */
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id=" + id));
        categoryRepository.delete(category);
    }
}