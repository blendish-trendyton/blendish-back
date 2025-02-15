package com.example.blendish.domain.gpt.dto;

import java.util.List;

public record CustomRecipeReqDTO(
        String category,
        int cookingTime,
        String difficulty,
        List<String> tastes,
        int spiceLevel
) {
}
