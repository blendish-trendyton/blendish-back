package com.example.blendish.domain.recipe.repository;

import com.example.blendish.domain.recipe.entity.Category;
import com.example.blendish.domain.recipe.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    List<Category> findByNameContaining(@Param("name") String name);

}
