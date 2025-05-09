package com.ecommerce.project.controller;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/public/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        return new ResponseEntity<>(categoryService.getAllCategories(), HttpStatus.OK);
    }

    @PostMapping("/admin/category")
    public ResponseEntity<String> addCategory(@RequestBody Category category) {
        if (categoryService.createCategory(category)){
            return new ResponseEntity<>("Category added successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Category already exist", HttpStatus.BAD_REQUEST);
    }
    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {
        if(categoryService.deleteCategory(categoryId)) {
            return new ResponseEntity<>("Category deleted successfully", HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Category does not exist", HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/admin/categories/{categoryId}")
    public ResponseEntity<String> updateCategory(@PathVariable Long categoryId, @RequestBody Category category) {
        if(categoryService.updateCategory(categoryId, category)) {
            return new ResponseEntity<>("Category updated successfully", HttpStatus.OK);
        }
        else
            return new ResponseEntity<>("Category does not exist", HttpStatus.BAD_REQUEST);
    }
}
