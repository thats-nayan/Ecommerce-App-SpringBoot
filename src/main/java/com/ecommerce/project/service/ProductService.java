package com.ecommerce.project.service;

import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductRequestDTO;
import com.ecommerce.project.payload.ProductResponseDTO;

public interface ProductService {
    ProductRequestDTO createProduct(Long categoryId, Product product);

    ProductResponseDTO getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponseDTO getProductsByCategory(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, Long categoryId);

    ProductResponseDTO getProductsByKeyword(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String keyword);
}
