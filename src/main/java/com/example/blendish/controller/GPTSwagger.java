package com.example.blendish.controller;

import com.example.blendish.domain.gpt.dto.CustomRecipeReqDTO;
import com.example.blendish.domain.recipe.dto.AddRecipeDTO;
import com.example.blendish.global.dto.ApiResponseTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "GPT API", description = "GPT 관련 API")
public interface GPTSwagger {

    @Operation(summary = "GPT 채팅", description = "GPT에게 메시지를 보내고 응답을 받습니다.")
    ResponseEntity<ApiResponseTemplate> chatWithGpt(@RequestParam String message);

    @Operation(summary = "사용자 맞춤 GPT 레시피 생성", description = "사용자 입력을 기반으로 GPT가 맞춤형 레시피를 생성합니다.")
    ResponseEntity<ApiResponseTemplate<String>> generateCustomRecipe(
            @RequestBody CustomRecipeReqDTO request,
            UserDetails userDetails
    );

    @Operation(summary = "AI 레시피 저장", description = "AI가 생성한 레시피를 데이터베이스에 저장합니다.")
    ResponseEntity<ApiResponseTemplate<String>> saveRecipe(
            @RequestBody AddRecipeDTO addRecipeDTO,
            UserDetails userDetails
    );
}
