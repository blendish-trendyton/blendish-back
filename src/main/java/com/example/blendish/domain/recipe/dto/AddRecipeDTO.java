package com.example.blendish.domain.recipe.dto;

import com.example.blendish.domain.recipe.entity.RecipeSteps;
import java.util.List;

public record AddRecipeDTO(String name,
                           String level,
                           String ingredients,
                           String information,
                           String foodImage,
                           List<RecipeStepDTO> steps) {
}
