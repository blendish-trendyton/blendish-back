package com.example.blendish.domain.recipe.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LiketenRecipeDTO {
    private Long recipeId;
    private String name;
}
