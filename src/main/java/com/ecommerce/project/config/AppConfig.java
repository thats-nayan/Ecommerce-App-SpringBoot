package com.ecommerce.project.config;

import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductRequestDTO;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        // Define mapping from Product → ProductRequestDTO
        modelMapper.typeMap(Product.class, ProductRequestDTO.class)
                .addMappings(mapper ->
                        mapper.map(src -> src.getCategory().getCategoryName(), ProductRequestDTO::setCategoryName)
                );
        return modelMapper;
    }
}
