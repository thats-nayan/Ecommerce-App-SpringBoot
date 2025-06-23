package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.EmptyResourceException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.CategoryRequestDTO;
import com.ecommerce.project.payload.CategoryResponseDTO;
import com.ecommerce.project.payload.ProductRequestDTO;
import com.ecommerce.project.payload.ProductResponseDTO;
import com.ecommerce.project.repositories.CategoryRepository;
import com.ecommerce.project.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public ProductRequestDTO createProduct(Long categoryId, Product product) {
        Category category = searchCategory(categoryId);

        product.setCategory(category);
        double specialPrice = product.getPrice() - (product.getPrice() * product.getDiscount() * 0.01);
        product.setSpecialPrice(specialPrice);
        product.setImage("default.png");

        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductRequestDTO.class);
    }

    @Override
    public ProductResponseDTO getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortObject = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        PageRequest pageDetails = PageRequest.of(pageNumber,pageSize,sortObject);
        Page<Product> productPage = productRepository.findAll(pageDetails);

        if(productPage.getTotalElements() == 0) {
            throw new EmptyResourceException("Products");
        }

        List<Product> products = productPage.getContent();
        List<ProductRequestDTO> productRequestDTOs = products.stream().map(product -> modelMapper.map(product, ProductRequestDTO.class)).toList();
        return getProductResponseDTO(productRequestDTOs, productPage);
    }

    private static ProductResponseDTO getProductResponseDTO(List<ProductRequestDTO> productRequestDTOS, Page<Product> productPage) {
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setContent(productRequestDTOS);

        // Set pagination metadata
        productResponseDTO.setTotalElements(productPage.getTotalElements());
        productResponseDTO.setPageNumber(productPage.getNumber());
        productResponseDTO.setPageSize(productPage.getSize());
        productResponseDTO.setTotalPages(productPage.getTotalPages());
        productResponseDTO.setLastPage(productPage.isLast());
        return productResponseDTO;
    }

    @Override
    public ProductResponseDTO getProductsByCategory(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, Long categoryId) {
        Category category = searchCategory(categoryId);
        Sort sortObject = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        PageRequest pageDetails = PageRequest.of(pageNumber,pageSize,sortObject);
        Page<Product> productPage = productRepository.findByCategory(category,pageDetails);

        if(productPage.getTotalElements() == 0) {
            throw new EmptyResourceException("Products");
        }

        List<Product> products = productPage.getContent();
        List<ProductRequestDTO> productRequestDTOs = products.stream().map(product -> modelMapper.map(product, ProductRequestDTO.class)).toList();
        return getProductResponseDTO(productRequestDTOs, productPage);
    }

    public ProductResponseDTO getProductsByKeyword(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String keyword){
        Sort sortObject = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        PageRequest pageDetails = PageRequest.of(pageNumber,pageSize,sortObject);
        Page<Product> productPage = productRepository.findByProductNameLikeIgnoreCase('%'+keyword+'%',pageDetails);

        if(productPage.getTotalElements() == 0) {
            throw new EmptyResourceException("Products");
        }

        List<Product> products = productPage.getContent();
        List<ProductRequestDTO> productRequestDTOs = products.stream().map(product -> modelMapper.map(product, ProductRequestDTO.class)).toList();
        return getProductResponseDTO(productRequestDTOs, productPage);
    }

    public Category searchCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category","CategoryId",categoryId));
    }

}
