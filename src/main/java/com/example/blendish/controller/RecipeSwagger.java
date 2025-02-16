package com.example.blendish.controller;

import com.example.blendish.domain.recipe.dto.AddUserRecipeDTO;
import com.example.blendish.global.dto.ApiResponseTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

public interface RecipeSwagger {

    @Tag(name = "Recipe Controller", description = "레시피 관련 API")
    @Operation(
            summary = "레시피 추가 API",
            description = "새로운 레시피를 추가하는 API입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "레시피 등록 성공",
                            content = @Content(schema = @Schema(implementation = ApiResponseTemplate.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            }
    )
    @PostMapping("/api/recipe")
    ResponseEntity<ApiResponseTemplate<String>> addRecipe(@RequestBody AddUserRecipeDTO addRecipeDTO,
                                                          @RequestPart(value = "image", required = false) MultipartFile image,
                                                          @AuthenticationPrincipal UserDetails userDetails) throws IOException;
}
