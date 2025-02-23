package com.example.blendish.domain.recipe.service;

import com.example.blendish.domain.recipe.dto.IngredientDTO;
import com.example.blendish.domain.recipe.dto.RecipeStepDTO;
import com.example.blendish.domain.recipe.entity.Ingredient;
import com.example.blendish.domain.recipe.entity.Recipe;
import com.example.blendish.domain.recipe.entity.RecipeSteps;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class RecipeMapper {

    public List<RecipeSteps> mapToRecipeSteps(List<RecipeStepDTO> stepDTOs, List<String> stepImageUrls, Recipe recipe) {
        List<RecipeSteps> steps = new ArrayList<>();
        for (int i = 0; i < stepDTOs.size(); i++) {
            RecipeStepDTO stepDTO = stepDTOs.get(i);
            String stepImageUrl = (i < stepImageUrls.size()) ? stepImageUrls.get(i) : null;

            steps.add(RecipeSteps.builder()
                    .stepNum(stepDTO.stepNumber())
                    .details(stepDTO.details())
                    .stepImage(stepImageUrl)
                    .recipe(recipe)
                    .build());
        }
        return steps;
    }

    public List<Ingredient> mapToIngredients(List<IngredientDTO> ingredientDTOs, Recipe recipe) {
        return ingredientDTOs.stream()
                .map(dto -> Ingredient.builder()
                        .name(dto.name())
                        .amount(dto.amount())
                        .recipe(recipe)
                        .build())
                .toList();
    }
}

