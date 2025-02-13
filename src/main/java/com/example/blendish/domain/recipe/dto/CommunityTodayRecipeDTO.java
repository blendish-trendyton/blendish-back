package com.example.blendish.domain.recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommunityTodayRecipeDTO {
    private Long recipeId;
    private String foodImage;
    private String name;
}
