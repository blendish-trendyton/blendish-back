package com.example.blendish.domain.recipe.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AIRecipeStepDTO {
    private String details;
    private int stemNum;
}
