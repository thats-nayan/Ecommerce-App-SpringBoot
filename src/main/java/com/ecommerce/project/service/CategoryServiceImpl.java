package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.EmptyResourceException;
import com.ecommerce.project.exceptions.ResourceAlreadyExistsException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        List <Category> categories = categoryRepository.findAll();
        if(categories.isEmpty()) {
            throw new EmptyResourceException("Categories");
        }
        return categories;
    }
    @Override
    public String createCategory(Category category) {
        Optional<Category> existing = categoryRepository.findByCategoryNameIgnoreCase(category.getCategoryName());
        if(existing.isPresent()) {
            throw new ResourceAlreadyExistsException("Category","categoryName",category.getCategoryName());
        }
        categoryRepository.save(category);
        return "Category added successfully";
    }
    @Override
    public String deleteCategory(Long categoryId) {
        Category existing = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category","CategoryId",categoryId));
        categoryRepository.delete(existing);
        return "Category deleted successfully";
    }
    @Override
    public String updateCategory(Long categoryId,Category updatedCategory) {
        Optional<Category> match = categoryRepository.findById(categoryId);
        if(match.isPresent()) {
            Category existingCategory = match.get();
            existingCategory.setCategoryName(updatedCategory.getCategoryName());
            categoryRepository.save(existingCategory);
            return "Category updated successfully";
        }
        throw new ResourceNotFoundException("Category","CategoryId",categoryId);
    }
}
