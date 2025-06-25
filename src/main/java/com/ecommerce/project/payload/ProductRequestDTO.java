package com.ecommerce.project.payload;

import com.ecommerce.project.utils.ValidatorGroups.OnCreate;
import com.ecommerce.project.utils.ValidatorGroups.OnUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDTO {
    private Long productId;

    @NotBlank(message = "Product name cannot be blank")
    @Size(min = 5,message = "Product name must have least 5 characters")
    private String productName;

    @NotBlank(groups = {OnCreate.class}, message = "Product description cannot be blank")
    @Size(groups = {OnCreate.class, OnUpdate.class} ,min = 5,message = "Product description must have least 5 characters")
    private String description;

    private String image;

    @Positive(groups = {OnCreate.class, OnUpdate.class},message = "Product quantity must be a positive number")
    private Integer quantity;

    @Positive(groups = {OnCreate.class, OnUpdate.class},message = "Product price must be a positive number")
    private Double price;

    private Double discount;
    private Double specialPrice;
    private String categoryName;
}
