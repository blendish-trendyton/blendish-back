package com.example.blendish.controller;

import com.example.blendish.global.dto.ApiResponseTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Recipe Controller", description = "레시피 관련 API")
public interface RecipeSwagger {

    @Operation(
            summary = "레시피 추가 API",
            description = "새로운 레시피를 추가하는 API입니다. JSON 데이터와 이미지를 함께 전송해야 합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "레시피 등록 성공",
                            content = @Content(schema = @Schema(implementation = ApiResponseTemplate.class))),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            }
    )
    @PostMapping(value = "/api/recipe", consumes = {"multipart/form-data"})
    ResponseEntity<ApiResponseTemplate<String>> addRecipe(
            @Parameter(description = "레시피 정보(JSON)", schema = @Schema(implementation = String.class))
            @RequestParam("addRecipeDTO") String addRecipeDTOJson,

            @Parameter(description = "레시피 대표 이미지", content = @Content(mediaType = "image/*"))
            @RequestPart(value = "image", required = false) MultipartFile image,

            @RequestPart(value = "stepImages", required = false) List<MultipartFile> stepImages,

            @Parameter(hidden = true)
            @AuthenticationPrincipal UserDetails userDetails
    ) throws IOException;
}
