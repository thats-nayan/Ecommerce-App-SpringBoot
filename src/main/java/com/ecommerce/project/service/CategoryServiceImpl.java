package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.EmptyResourceException;
import com.ecommerce.project.exceptions.ResourceAlreadyExistsException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryRequestDTO;
import com.ecommerce.project.payload.CategoryResponseDTO;
import com.ecommerce.project.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public CategoryResponseDTO getAllCategories() {
        List <Category> categories = categoryRepository.findAll();
        if(categories.isEmpty()) {
            throw new EmptyResourceException("Categories");
        }
        List<CategoryRequestDTO> categoryRequestDTOS = categories.stream().map(category -> modelMapper.map(category, CategoryRequestDTO.class)).toList();
        CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();
        categoryResponseDTO.setContent(categoryRequestDTOS);
        return categoryResponseDTO;
    }
    @Override
    public CategoryRequestDTO createCategory(CategoryRequestDTO category) {
        Optional<Category> existing = categoryRepository.findByCategoryNameIgnoreCase(category.getCategoryName());
        if(existing.isPresent()) {
            throw new ResourceAlreadyExistsException("Category","categoryName",category.getCategoryName());
        }
        Category newCategory = modelMapper.map(category, Category.class);
        return modelMapper.map(categoryRepository.save(newCategory), CategoryRequestDTO.class);
    }
    @Override
    public CategoryRequestDTO deleteCategory(Long categoryId) {
        Category existing = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category","CategoryId",categoryId));
        categoryRepository.delete(existing);
        return modelMapper.map(existing, CategoryRequestDTO.class);
    }
    @Override
    public CategoryRequestDTO updateCategory(Long categoryId,CategoryRequestDTO updatedCategory) {
        Optional<Category> match = categoryRepository.findById(categoryId);
        if(match.isPresent()) {
            Category existingCategory = match.get();
            existingCategory.setCategoryName(updatedCategory.getCategoryName());
            return modelMapper.map(categoryRepository.save(existingCategory), CategoryRequestDTO.class);
        }
        throw new ResourceNotFoundException("Category","CategoryId",categoryId);
    }
}
