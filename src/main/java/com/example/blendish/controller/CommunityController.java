package com.example.blendish.controller;

import com.example.blendish.domain.recipe.dto.CommunityHotRecipeDTO;
import com.example.blendish.domain.recipe.dto.CommunityTodayRecipeDTO;
import com.example.blendish.domain.recipe.service.CommunityService;
import com.example.blendish.global.dto.ApiResponseTemplate;
import com.example.blendish.global.response.SuccessCode;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Community Controller", description = "커뮤니티 관련 API")
@RestController
@RequestMapping("/api/community")
@AllArgsConstructor
public class CommunityController {

    private final CommunityService communityService;


    @GetMapping("/HotRecipe")
    public ResponseEntity<ApiResponseTemplate<List<CommunityHotRecipeDTO>>> getHot() {

        List<CommunityHotRecipeDTO> hotRecipyDTOS = communityService.getTopLikedRecipes();

        return ResponseEntity.ok(ApiResponseTemplate.success(SuccessCode.OK, hotRecipyDTOS));
    }

    @GetMapping("/TodayRecipe")
    public ResponseEntity<ApiResponseTemplate<List<CommunityTodayRecipeDTO>>> getToday() {

        List<CommunityTodayRecipeDTO> todayRecipe = communityService.getTodayRecipe();

        return ResponseEntity.ok(ApiResponseTemplate.success(SuccessCode.OK, todayRecipe));
    }


}
