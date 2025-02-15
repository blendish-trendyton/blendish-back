package com.example.blendish.controller;

import com.example.blendish.domain.gpt.dto.CustomRecipeReqDTO;
import com.example.blendish.domain.gpt.service.GPTRecipeService;
import com.example.blendish.domain.gpt.service.OpenAIService;
import com.example.blendish.global.dto.ApiResponseTemplate;
import com.example.blendish.global.response.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gpt")
public class GPTController {

    private final OpenAIService openAIService;
    private final GPTRecipeService gptRecipeService;

    @GetMapping("/chat")
    public Mono<String> chatWithGpt(@RequestParam String message) {
        return openAIService.getGptResponse(message);
    }

    @PostMapping("/recipe")
    public Mono<ResponseEntity<ApiResponseTemplate<String>>> generateCustomRecipe(
            @RequestBody CustomRecipeReqDTO request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return gptRecipeService.getAiGeneratedRecipe(request, userDetails)
                .map(result -> ResponseEntity.ok(ApiResponseTemplate.success(SuccessCode.CREATED, result)));
    }

}
