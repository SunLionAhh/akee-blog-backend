package com.akee.blog.service;

import com.akee.blog.dto.CategoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    CategoryDTO createCategory(CategoryDTO categoryDTO);
    CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO);
    void deleteCategory(Long id);
    CategoryDTO getCategoryById(Long id);
    Page<CategoryDTO> getAllCategories(Pageable pageable);
} 