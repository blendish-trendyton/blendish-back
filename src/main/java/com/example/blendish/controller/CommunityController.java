package com.example.blendish.controller;

import com.example.blendish.domain.recipe.dto.CommunityDetailDTO;
import com.example.blendish.domain.recipe.dto.CommunityHotRecipeDTO;
import com.example.blendish.domain.recipe.dto.CommunityTodayRecipeDTO;
import com.example.blendish.domain.recipe.service.CommunityService;
import com.example.blendish.global.dto.ApiResponseTemplate;
import com.example.blendish.global.response.SuccessCode;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Community Controller", description = "커뮤니티 관련 API")
@RestController
@RequestMapping("/api/community")
@AllArgsConstructor
public class CommunityController {

    private final CommunityService communityService;

    //hot 레시피
    @GetMapping("/HotRecipe")
    public ResponseEntity<ApiResponseTemplate<List<CommunityHotRecipeDTO>>> getHot() {

        List<CommunityHotRecipeDTO> hotRecipyDTOS = communityService.getTopLikedRecipes();

        return ResponseEntity.ok(ApiResponseTemplate.success(SuccessCode.OK, hotRecipyDTOS));
    }
    //오늘의 레시피
    @GetMapping("/TodayRecipe")
    public ResponseEntity<ApiResponseTemplate<List<CommunityTodayRecipeDTO>>> getToday() {

        List<CommunityTodayRecipeDTO> todayRecipe = communityService.getTodayRecipe();

        return ResponseEntity.ok(ApiResponseTemplate.success(SuccessCode.OK, todayRecipe));
    }
    //레시피 디테일
    @GetMapping("/DetailRecipe")
    public ResponseEntity<ApiResponseTemplate<CommunityDetailDTO>> getDetail(@RequestParam(name = "recipeId") Long recipeId) {

        CommunityDetailDTO detailDTOS = communityService.getDetail(recipeId);

        return ResponseEntity.ok(ApiResponseTemplate.success(SuccessCode.OK, detailDTOS));
    }

    // 좋아요 클릭시
//    @PostMapping("/updateLike")
//    public ResponseEntity<ApiResponseTemplate<?>> updateLike(@RequestBody Long recipeId) {
//
//        communityService.insertLike(recipeId);
//
//        return ResponseEntity.ok(ApiResponseTemplate.success(SuccessCode.OK, null ));
//    }
//
//    // 좋아요 삭제시
//    @PostMapping("/deleteLike")
//    public ResponseEntity<ApiResponseTemplate<?>> deleteLike(@RequestBody Long recipeId) {
//
//        communityService.removeLike(recipeId);
//
//        return ResponseEntity.ok(ApiResponseTemplate.success(SuccessCode.OK, null ));
//    }
//
//    // 스크랩 클릭시
//    @PostMapping("/updateScrap")
//    public ResponseEntity<ApiResponseTemplate<?>> updatScrap(@RequestBody Long recipeId) {
//
//        communityService.insertScrap(recipeId);
//
//        return ResponseEntity.ok(ApiResponseTemplate.success(SuccessCode.OK, null ));
//    }




}
