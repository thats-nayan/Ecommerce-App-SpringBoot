package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    private Long uniqueId = (long)0;
    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
    @Override
    public boolean createCategory(Category category) {
        Optional<Category> existing = categoryRepository.findByCategoryNameIgnoreCase(category.getCategoryName());
        if(existing.isPresent()) {
            return false;
        }
        categoryRepository.save(category);
        return true;
    }
    @Override
    public boolean deleteCategory(Long categoryId) {
        Optional<Category> existing = categoryRepository.findById(categoryId);
        existing.ifPresent(category -> categoryRepository.delete(category));
        return existing.isPresent();
    }
    @Override
    public boolean updateCategory(Long categoryId,Category updatedCategory) {
        Optional<Category> match = categoryRepository.findById(categoryId);
        if(match.isPresent()) {
            if(updatedCategory.getCategoryName().equalsIgnoreCase(match.get().getCategoryName())) {
                return true;
            }
            else {
                categoryRepository.delete(match.get());
                categoryRepository.save(updatedCategory);
            }
        }
        return match.isPresent();
    }
}
