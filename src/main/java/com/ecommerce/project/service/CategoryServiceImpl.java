package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final List<Category> categories = new ArrayList<Category>();
    private Long uniqueId = (long)0;
    @Override
    public List<Category> getAllCategories() {
        return categories;
    }
    @Override
    public boolean createCategory(Category category) {
        Optional<Category> existing = categories.stream()
                .filter(c -> c.getCategoryName().equalsIgnoreCase(category.getCategoryName()))
                .findFirst();
        if(existing.isPresent()) {
            return false;
        }
        uniqueId++;
        category.setCategoryId(uniqueId);
        categories.add(category);
        return true;
    }
    @Override
    public boolean deleteCategory(Long categoryId) {
        return categories.removeIf(category -> categoryId.equals(category.getCategoryId()));
    }
    @Override
    public boolean updateCategory(Long categoryId,Category updatedCategory) {
        Optional<Category> match = categories.stream()
                .filter(c -> c.getCategoryId().equals(categoryId))
                .findFirst();
        match.ifPresent(category -> {
            if(updatedCategory.getCategoryName() != null)
                category.setCategoryName(updatedCategory.getCategoryName());
        });
        return match.isPresent();
    }
}
