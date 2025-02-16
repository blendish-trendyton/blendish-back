package com.example.blendish.domain.recipe.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiIngredientDTO {
    private String amount;
    private String name;
}
