package com.ecommerce.project.controller;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryRequestDTO;
import com.ecommerce.project.payload.CategoryResponseDTO;
import com.ecommerce.project.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.ecommerce.project.config.AppConstants.*;

@RestController
@RequestMapping("/api")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponseDTO> getAllCategories(
            @RequestParam(name = "pageNumber",defaultValue = PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize",defaultValue = PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy",defaultValue = SORT_BY_CATEGORY_NAME) String sortBy,
            @RequestParam(name = "sortOrder",defaultValue = SORT_DIRECTION) String sortOrder
    ) {
        return new ResponseEntity<>(categoryService.getAllCategories(pageNumber,pageSize,sortBy,sortOrder), HttpStatus.OK);
    }
    @PostMapping("/admin/category")
    public ResponseEntity<CategoryRequestDTO> addCategory(@Valid @RequestBody CategoryRequestDTO category) {
        return new ResponseEntity<>(categoryService.createCategory(category), HttpStatus.OK);
    }
    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryRequestDTO> deleteCategory(@PathVariable Long categoryId) {
        return new ResponseEntity<>(categoryService.deleteCategory(categoryId), HttpStatus.OK);
    }
    @PutMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryRequestDTO> updateCategory(@PathVariable Long categoryId,@Valid @RequestBody CategoryRequestDTO category) {
        return new ResponseEntity<>(categoryService.updateCategory(categoryId, category), HttpStatus.OK);
    }
}
