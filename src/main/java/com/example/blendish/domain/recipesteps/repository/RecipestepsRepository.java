package com.example.blendish.domain.recipesteps.repository;

import com.example.blendish.domain.recipe.dto.RecipeStepDTO;
import com.example.blendish.domain.recipe.entity.AiIngredient;
import com.example.blendish.domain.recipe.entity.RecipeSteps;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecipestepsRepository extends JpaRepository<RecipeSteps,Long> {
    RecipeSteps findByRecipeRecipeId(Long recipeId);


}
