package com.example.blendish.controller;

import com.example.blendish.domain.recipe.dto.AddRecipeDTO;
import com.example.blendish.domain.recipe.entity.Recipe;
import com.example.blendish.domain.recipe.service.RecipeService;
import com.example.blendish.global.dto.ApiResponseTemplate;
import com.example.blendish.global.response.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recipe")
public class RecipeController implements RecipeSwagger{

    private final RecipeService recipeService;

    @PostMapping
    public ResponseEntity<ApiResponseTemplate<String>> addRecipe(@RequestBody AddRecipeDTO addRecipeDTO) {
        recipeService.createRecipe(addRecipeDTO);
        return ResponseEntity.ok(ApiResponseTemplate.success(SuccessCode.OK, "레시피가 등록되었습니다."));
    }
}
