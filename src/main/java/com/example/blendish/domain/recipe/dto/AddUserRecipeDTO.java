package com.example.blendish.domain.recipe.dto;

import com.example.blendish.domain.recipe.entity.Ingredient;
import java.util.List;

public record AddUserRecipeDTO(String name,
                           String level,
                           String time,
                           List<Ingredient> ingredients,
                           String information,
                           boolean isAiGenerated,
                           List<RecipeStepDTO> steps) {
}
