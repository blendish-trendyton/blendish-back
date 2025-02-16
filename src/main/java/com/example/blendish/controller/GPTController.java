package com.example.blendish.controller;

import com.example.blendish.domain.gpt.dto.CustomRecipeReqDTO;
import com.example.blendish.domain.gpt.service.GPTRecipeService;
import com.example.blendish.domain.gpt.service.OpenAIService;
import com.example.blendish.domain.recipe.dto.AddAiRecipeDTO;
import com.example.blendish.domain.recipe.service.RecipeService;
import com.example.blendish.global.dto.ApiResponseTemplate;
import com.example.blendish.global.response.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gpt")
public class GPTController implements GPTSwagger{

    private final OpenAIService openAIService;
    private final GPTRecipeService gptRecipeService;
    private final RecipeService recipeService;

    @GetMapping("/chat")
    public ResponseEntity<ApiResponseTemplate> chatWithGpt(@RequestParam String message) {
        return ResponseEntity.ok(ApiResponseTemplate.success(SuccessCode.OK, openAIService.getGptResponse(message)));

    }

    @PostMapping("/recipe")
    public ResponseEntity<ApiResponseTemplate<String>> generateCustomRecipe(
            @RequestBody CustomRecipeReqDTO request,
            @AuthenticationPrincipal UserDetails userDetails) {

        String result = gptRecipeService.getAiGeneratedRecipe(request, userDetails);

        return ResponseEntity.ok(ApiResponseTemplate.success(SuccessCode.CREATED, result));
    }

    @PostMapping("/recipe/save")
    public ResponseEntity<ApiResponseTemplate<String>> saveRecipe(@RequestBody AddAiRecipeDTO addRecipeDTO, @AuthenticationPrincipal UserDetails userDetails) {
        recipeService.createAiRecipe(addRecipeDTO, userDetails.getUsername());

        return ResponseEntity.ok(ApiResponseTemplate.success(SuccessCode.CREATED, "레시피가 등록되었습니다."));
    }

}
