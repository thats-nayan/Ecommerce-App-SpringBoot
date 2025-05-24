package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> getAllCategories();
    String createCategory(Category category);
    String deleteCategory(Long categoryId);
    String updateCategory(Long categoryId,Category category);
}
