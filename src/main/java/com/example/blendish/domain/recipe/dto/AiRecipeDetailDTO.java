package com.example.blendish.domain.recipe.dto;

import com.example.blendish.domain.recipe.entity.AiIngredient;
import com.example.blendish.domain.recipe.entity.AiRecipeSteps;
import com.example.blendish.domain.recipe.entity.Ingredient;
import com.example.blendish.domain.recipesteps.dto.RecipeStepsDTO;
import lombok.*;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiRecipeDetailDTO {
    private Long recipeId;
    private String level;
    private String time;
    private String name;
    private List<AiIngredientDTO> ingredients;
    private List<AIRecipeStepDTO> recipeSteps;
}
