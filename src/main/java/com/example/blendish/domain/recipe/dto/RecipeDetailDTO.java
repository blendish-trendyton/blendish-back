package com.example.blendish.domain.recipe.dto;

import com.example.blendish.domain.comments.dto.CommentDTO;
import com.example.blendish.domain.recipe.entity.Ingredient;
import com.example.blendish.domain.recipe.entity.RecipeSteps;
import com.example.blendish.domain.recipesteps.dto.RecipeStepsDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeDetailDTO {
    private Long recipeId;
    private String level;
    private String time;
    private String name;
    private List<Ingredient> ingredients;
    private RecipeStepsDTO recipeSteps;

}
