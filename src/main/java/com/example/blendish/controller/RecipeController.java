package com.example.blendish.controller;

import com.example.blendish.domain.recipe.dto.AddUserRecipeDTO;
import com.example.blendish.domain.recipe.service.RecipeService;
import com.example.blendish.global.dto.ApiResponseTemplate;
import com.example.blendish.global.response.SuccessCode;
import com.example.blendish.global.s3.S3UploadService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recipe")
public class RecipeController implements RecipeSwagger{

    private final RecipeService recipeService;
    private final S3UploadService s3UploadService;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResponseTemplate<String>> addRecipe(
            @RequestParam("addRecipeDTO") String addRecipeDTOJson,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @AuthenticationPrincipal UserDetails userDetails) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        AddUserRecipeDTO addRecipeDTO = objectMapper.readValue(addRecipeDTOJson, AddUserRecipeDTO.class);

        String imageUrl = (image != null && !image.isEmpty()) ? s3UploadService.saveFile(image) : null;
        recipeService.createUserRecipe(addRecipeDTO, userDetails.getUsername(), imageUrl);

        return ResponseEntity.ok(ApiResponseTemplate.success(SuccessCode.OK, "레시피가 등록되었습니다."));
    }

}
