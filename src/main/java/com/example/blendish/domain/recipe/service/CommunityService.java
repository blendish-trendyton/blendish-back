package com.example.blendish.domain.recipe.service;

import com.example.blendish.domain.comments.repository.CommentsRepository;
import com.example.blendish.domain.recipe.dto.CommunityHotRecipyDTO;
import com.example.blendish.domain.recipe.entity.Recipe;
import com.example.blendish.domain.recipe.repository.LikeRepository;
import com.example.blendish.domain.recipe.repository.RecipeRepository;
import io.jsonwebtoken.lang.Collections;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CommunityService {
    private final RecipeRepository recipeRepository;
    private final LikeRepository likeRepository;
    private final CommentsRepository commentsRepository;

    // 인기 레시피 가져오는 서비스
    public List<CommunityHotRecipyDTO> getTopLikedRecipes() {

        List<CommunityHotRecipyDTO> topLikedRecipi = new ArrayList<>();

        // 좋아요 많은 id 가져오기
        List<Long> topLikedRecipeIds = likeRepository.findTopLikedRecipeIds();


        if (topLikedRecipeIds.isEmpty()){
            return Collections.emptyList();
        }

        // 가져온거 4번 돌리면서 dto 에 집어넣기
        for (int i = 0; i < Math.min(topLikedRecipeIds.size(), 4); i++) {
            Long recipeId = topLikedRecipeIds.get(i);
            // 레시피 객체 가져오기
            Recipe recipe = recipeRepository.findByRecipeId(recipeId);
            // 코멘트 개수 가져오기
            int CommentNum = commentsRepository.countByRecipeRecipeId(recipeId);

            CommunityHotRecipyDTO topRecipes = new CommunityHotRecipyDTO(recipeId,recipe.getFoodImage(),recipe.getLikeCount(),CommentNum);


            topLikedRecipi.add(topRecipes);
        }

        return topLikedRecipi;
    }
}
