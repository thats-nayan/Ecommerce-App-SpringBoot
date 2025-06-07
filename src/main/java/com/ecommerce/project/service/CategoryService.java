package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryRequestDTO;
import com.ecommerce.project.payload.CategoryResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    CategoryResponseDTO getAllCategories(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder);
    CategoryRequestDTO createCategory(CategoryRequestDTO categoryRequestDTO);
    CategoryRequestDTO deleteCategory(Long categoryId);
    CategoryRequestDTO updateCategory(Long categoryId,CategoryRequestDTO categoryRequestDTO);
}
