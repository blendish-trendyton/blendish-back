package com.example.blendish.domain.recipe.dto;

import org.springframework.web.multipart.MultipartFile;

public record RecipeStepDTO(
        int stepNumber,String details, MultipartFile stepImage
) {
}
