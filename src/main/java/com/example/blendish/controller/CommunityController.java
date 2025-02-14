package com.example.blendish.controller;

import com.example.blendish.domain.recipe.dto.*;
import com.example.blendish.domain.recipe.service.CommunityService;
import com.example.blendish.global.dto.ApiResponseTemplate;
import com.example.blendish.global.response.ErrorCode;
import com.example.blendish.global.response.SuccessCode;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Printable;
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
    @PostMapping("/updateLike/{recipeId}")
    public ResponseEntity<ApiResponseTemplate<?>> updateLike(@PathVariable("recipeId") Long recipeId) {
        try {
            if(!communityService.lsHaveLike(recipeId)){
                return  ResponseEntity.ok(ApiResponseTemplate.error(ErrorCode.INTERNAL_SERVER_ERROR));
            }
            communityService.insertLike(recipeId);
            return ResponseEntity.ok(ApiResponseTemplate.success(SuccessCode.OK, null));

        } catch (Exception ex) {
            return  ResponseEntity.ok(ApiResponseTemplate.error(ErrorCode.INTERNAL_SERVER_ERROR));
        }
    }


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

    //레시피 전체 디테일
    @GetMapping("/AllDetailRecipe")
    public ResponseEntity<ApiResponseTemplate<RecipeDetailDTO>> getAllDetail(@RequestParam(name = "recipeId") Long recipeId) {

        RecipeDetailDTO recipeDetailDTO = communityService.getAllDetail(recipeId);

        return ResponseEntity.ok(ApiResponseTemplate.success(SuccessCode.OK, recipeDetailDTO));
    }

    // 상위 10 개 레시피
    @GetMapping("/getTenhigher")
    public ResponseEntity<ApiResponseTemplate<List<LiketenRecipeDTO>>> getTenHigher(){

        List<LiketenRecipeDTO> likeTenRecipeDTO = communityService.getTenRecipe();

        return ResponseEntity.ok(ApiResponseTemplate.success(SuccessCode.OK, likeTenRecipeDTO));
    }


}
