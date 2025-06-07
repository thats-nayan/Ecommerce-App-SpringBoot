package com.ecommerce.project.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequestDTO {
    private Long categoryId;
    @NotBlank(message = "Category name cannot be blank")
    @Size(min = 5,message = "Category name must have least 5 characters")
    private String categoryName;
}
