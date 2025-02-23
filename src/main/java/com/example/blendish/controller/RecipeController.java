package com.example.blendish.controller;

import com.example.blendish.domain.recipe.service.RecipeService;
import com.example.blendish.global.dto.ApiResponseTemplate;
import com.example.blendish.global.response.SuccessCode;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recipe")
public class RecipeController implements RecipeSwagger {

    private final RecipeService recipeService;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResponseTemplate<String>> addRecipe(
            @RequestParam("addRecipeDTO") String addRecipeDTOJson,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart(value = "stepImages", required = false) List<MultipartFile> stepImages,
            @AuthenticationPrincipal UserDetails userDetails) throws IOException {

        recipeService.createUserRecipe(addRecipeDTOJson, userDetails.getUsername(), image, stepImages);

        return ResponseEntity.ok(ApiResponseTemplate.success(SuccessCode.OK, "레시피가 등록되었습니다."));
    }
}

