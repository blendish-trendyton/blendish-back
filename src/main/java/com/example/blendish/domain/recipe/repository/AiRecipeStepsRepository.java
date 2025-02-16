package com.example.blendish.domain.recipe.repository;

import com.example.blendish.domain.recipe.entity.AiRecipeSteps;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AiRecipeStepsRepository extends JpaRepository<AiRecipeSteps,Long> {
    List<AiRecipeSteps> findByRecipeRecipeId(Long recipeId);
}
