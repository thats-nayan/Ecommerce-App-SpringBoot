package com.ecommerce.project.repositories;

import com.ecommerce.project.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Two args in JpaRepository (Entity name,data type of primary key of entity)
// No need to write implementation this interface (spring will do it)
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByCategoryNameIgnoreCase(String categoryName);
}
