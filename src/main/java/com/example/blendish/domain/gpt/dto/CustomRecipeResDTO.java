package com.example.blendish.domain.gpt.dto;

public record CustomRecipeResDTO(
        CustomRecipeReqDTO request,
        String generatedRecipe
) {
}
