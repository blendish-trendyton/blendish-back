package com.example.blendish.domain.recipe.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
public class RecipeSummaryDTO {
    private Long recipeId;
    private String foodImage;
    private String name;
}