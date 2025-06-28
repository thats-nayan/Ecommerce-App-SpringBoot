package com.ecommerce.project.controller;

import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductRequestDTO;
import com.ecommerce.project.payload.ProductResponseDTO;
import com.ecommerce.project.service.ProductService;
import com.ecommerce.project.utils.ValidatorGroups;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.ecommerce.project.config.AppConstants.*;

@RestController
@RequestMapping("/api")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductRequestDTO> addProduct(@Validated(ValidatorGroups.OnCreate.class) @RequestBody ProductRequestDTO product, @PathVariable Long categoryId) {
        return new ResponseEntity<>(productService.createProduct(categoryId,product), HttpStatus.OK);
    }

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponseDTO> getAllProducts(
            @RequestParam(name = "pageNumber",defaultValue = PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize",defaultValue = PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy",defaultValue = SORT_BY_PRODUCT_NAME) String sortBy,
            @RequestParam(name = "sortOrder",defaultValue = SORT_DIRECTION) String sortOrder) {
        return new ResponseEntity<>(productService.getAllProducts(pageNumber,pageSize,sortBy,sortOrder),HttpStatus.OK);
    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponseDTO> getProductsByCategory(
            @RequestParam(name = "pageNumber",defaultValue = PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize",defaultValue = PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy",defaultValue = SORT_BY_PRODUCT_NAME) String sortBy,
            @RequestParam(name = "sortOrder",defaultValue = SORT_DIRECTION) String sortOrder,
            @PathVariable Long categoryId) {
        return new ResponseEntity<>(productService.getProductsByCategory(pageNumber,pageSize,sortBy,sortOrder,categoryId),HttpStatus.OK);
    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponseDTO> getProductsByKeyword(
            @RequestParam(name = "pageNumber",defaultValue = PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize",defaultValue = PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy",defaultValue = SORT_BY_PRODUCT_NAME) String sortBy,
            @RequestParam(name = "sortOrder",defaultValue = SORT_DIRECTION) String sortOrder,
            @PathVariable String keyword) {
        return new ResponseEntity<>(productService.getProductsByKeyword(pageNumber,pageSize,sortBy,sortOrder,keyword),HttpStatus.FOUND);
    }

    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductRequestDTO> updateProduct(@Validated(ValidatorGroups.OnUpdate.class) @RequestBody ProductRequestDTO product, @PathVariable Long productId) {
        return new ResponseEntity<>(productService.updateProduct(productId,product), HttpStatus.OK);
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductRequestDTO> deleteProduct(@PathVariable Long productId) {
        return new ResponseEntity<>(productService.deleteProduct(productId), HttpStatus.OK);
    }

    @PutMapping("/admin/products/{productId}/image")
    public ResponseEntity<ProductRequestDTO> updateProductImage(
             @RequestParam("image") MultipartFile image,
             @PathVariable Long productId) throws IOException {
        return new ResponseEntity<>(productService.updateProductImage(productId,image), HttpStatus.OK);
    }
}
