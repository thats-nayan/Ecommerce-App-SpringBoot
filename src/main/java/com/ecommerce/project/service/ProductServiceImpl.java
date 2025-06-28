package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.EmptyResourceException;
import com.ecommerce.project.exceptions.ResourceAlreadyExistsException;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private FileService fileService;
    @Value("${project.images}")
    private String path;

    @Override
    public ProductRequestDTO createProduct(Long categoryId, ProductRequestDTO productRequestDTO) {
        Optional<Product> productExists = productRepository.findByProductNameIgnoreCase(productRequestDTO.getProductName());
        if(productExists.isPresent()){
            throw new ResourceAlreadyExistsException("Product","productName",productRequestDTO.getProductName());
        }
        Category category = searchCategory(categoryId);

        Product product = modelMapper.map(productRequestDTO, Product.class);
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
            throw new EmptyResourceException("Products","Category",category.getCategoryName());
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
            throw new EmptyResourceException("Products",keyword);
        }

        List<Product> products = productPage.getContent();
        List<ProductRequestDTO> productRequestDTOs = products.stream().map(product -> modelMapper.map(product, ProductRequestDTO.class)).toList();
        return getProductResponseDTO(productRequestDTOs, productPage);
    }

    @Override
    public ProductRequestDTO updateProduct(Long productId, ProductRequestDTO productRequestDTO) {
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("Product","ProductId",productId));

        if(productRequestDTO.getCategoryName() != null) {
            Category category = categoryRepository.findByCategoryNameIgnoreCase(productRequestDTO.getCategoryName())
                    .orElseThrow(()-> new ResourceNotFoundException("Category","CategoryName",productRequestDTO.getCategoryName()));
            product.setCategory(category);
        }
        if(productRequestDTO.getProductName() != null) {
            product.setProductName(productRequestDTO.getProductName());
        }
        if(productRequestDTO.getDescription() != null){
            product.setDescription(productRequestDTO.getDescription());
        }
        if(productRequestDTO.getQuantity() != null) {
            product.setQuantity(productRequestDTO.getQuantity());
        }
        if(productRequestDTO.getPrice() != null) {
            product.setPrice(productRequestDTO.getPrice());
        }
        if(productRequestDTO.getDiscount() != null) {
            product.setDiscount(productRequestDTO.getDiscount());
        }
        double specialPrice = product.getPrice() - (product.getPrice() * product.getDiscount() * 0.01);
        product.setSpecialPrice(specialPrice);
        return modelMapper.map(productRepository.save(product), ProductRequestDTO.class);
    }

    @Override
    public ProductRequestDTO deleteProduct(Long productId){
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("Product","ProductId",productId));
        productRepository.delete(product);
        return modelMapper.map(product, ProductRequestDTO.class);
    }

    @Override
    public ProductRequestDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("Product","ProductId",productId));

        // Upload image to server (file system in our case)
        // Get auto generated file name of uploaded image
        String fileName = fileService.uploadImage(path,image);

        // Updating the new product image for product
        product.setImage(fileName);

        // Return DTO after updating product
        return modelMapper.map(productRepository.save(product), ProductRequestDTO.class);
    }

    public Category searchCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category","CategoryId",categoryId));
    }
}
