package com.example.blendish.domain.recipe.dto;

import com.example.blendish.domain.foodflavor.entity.FoodFlavor;
import com.example.blendish.domain.recipe.entity.AiIngredient;
import java.util.List;

public record AddAiRecipeDTO(String name,
                               String level,
                               String time,
                               List<AiIngredient> AiIngredients,
                               String information,
                               boolean isAiGenerated,
                               List<FoodFlavor> foodFlavors,
                               List<RecipeStepDTO> steps) {
}
