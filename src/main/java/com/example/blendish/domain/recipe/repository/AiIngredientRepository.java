package com.example.blendish.domain.recipe.repository;

import com.example.blendish.domain.recipe.entity.AiIngredient;
import com.example.blendish.domain.recipe.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AiIngredientRepository extends JpaRepository<AiIngredient,Long> {
    List<AiIngredient> findByRecipeRecipeId(Long recipeId);
}
