package com.example.blendish.domain.recipesteps.repository;

import com.example.blendish.domain.recipe.dto.RecipeStepDTO;
import com.example.blendish.domain.recipe.entity.RecipeSteps;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipestepsRepository extends JpaRepository<RecipeSteps,Long> {
    RecipeSteps findByRecipeRecipeId(Long recipeId);
}
