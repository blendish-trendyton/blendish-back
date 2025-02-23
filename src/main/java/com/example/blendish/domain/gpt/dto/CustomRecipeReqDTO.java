package com.example.blendish.domain.gpt.dto;

import java.util.List;

public record CustomRecipeReqDTO(
        String category,
        String cookingTime,
        String difficulty,
        List<String> tastes,
        int spiceLevel
) {
}
