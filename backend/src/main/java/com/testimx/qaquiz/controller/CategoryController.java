package com.testimx.qaquiz.controller;

import com.testimx.qaquiz.dto.CategoryDto;
import com.testimx.qaquiz.dto.CategoryRequest;
import com.testimx.qaquiz.dto.MessageResponse;
import com.testimx.qaquiz.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.testimx.qaquiz.dto.MessageResponse;

import java.util.List;

/**
 * REST controller for categories.  Provides endpoints for listing
 * categories as well as administrative CRUD operations.
 */
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * Returns all quiz categories available in the system.  Any
     * authenticated user can call this endpoint.
     */
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    /**
     * Creates a new category.  Restricted to administrators.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(categoryService.createCategory(request));
    }

    /**
     * Updates an existing category.  Restricted to administrators.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(categoryService.updateCategory(id, request));
    }

    /**
     * Deletes a category by its id.  Restricted to administrators.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(new MessageResponse("Category deleted"));
    }
}