package com.example.blendish.domain.recipe.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeListDTO {
    private Long recipeId;
    private String foodImage;
    private int likeCount;
    private int commentCount;
    private String name;
    private String information;
}
