package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> getAllCategories();
    boolean createCategory(Category category);
    boolean deleteCategory(Long categoryId);
    boolean updateCategory(Long categoryId,Category category);
}
