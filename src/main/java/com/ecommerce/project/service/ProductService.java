package com.ecommerce.project.service;

import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductRequestDTO;
import com.ecommerce.project.payload.ProductResponseDTO;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ProductRequestDTO createProduct(Long categoryId, ProductRequestDTO product);

    ProductResponseDTO getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponseDTO getProductsByCategory(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, Long categoryId);

    ProductResponseDTO getProductsByKeyword(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String keyword);

    ProductRequestDTO updateProduct(Long productId,ProductRequestDTO product);

    ProductRequestDTO deleteProduct(Long productId);

    ProductRequestDTO updateProductImage(Long productId, MultipartFile image) throws IOException;
}
