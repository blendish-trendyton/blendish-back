package com.example.blendish.domain.user.dto.preference;

import com.example.blendish.domain.recipe.dto.RecipeSummaryDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserDetailDTO {
    private String userId;
    private String profilePic;
    private String hometown;
    private String country;
    private String role;
    private List<RecipeSummaryDTO> myRecipes;     // 사용자가 직접 등록한 레시피 (요약, 최대 4개)
    private List<RecipeSummaryDTO> savedRecipes;    // 사용자가 스크랩한 레시피 (요약, 최대 4개)
}