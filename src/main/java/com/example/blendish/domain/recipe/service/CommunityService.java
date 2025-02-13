package com.example.blendish.domain.recipe.service;

import com.example.blendish.domain.comments.dto.CommentDTO;
import com.example.blendish.domain.comments.entity.Comment;
import com.example.blendish.domain.comments.repository.CommentsRepository;
import com.example.blendish.domain.recipe.dto.CommunityDetailDTO;
import com.example.blendish.domain.recipe.dto.CommunityHotRecipeDTO;
import com.example.blendish.domain.recipe.dto.CommunityTodayRecipeDTO;
import com.example.blendish.domain.recipe.entity.Recipe;
import com.example.blendish.domain.recipe.repository.LikeRepository;
import com.example.blendish.domain.recipe.repository.RecipeRepository;
import io.jsonwebtoken.lang.Collections;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommunityService {
    private final RecipeRepository recipeRepository;
    private final LikeRepository likeRepository;
    private final CommentsRepository commentsRepository;

    // 인기 레시피 가져오는 서비스
    public List<CommunityHotRecipeDTO> getTopLikedRecipes() {

        List<CommunityHotRecipeDTO> topLikedRecipi = new ArrayList<>();

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

            CommunityHotRecipeDTO topRecipes = new CommunityHotRecipeDTO(recipeId,recipe.getFoodImage(),recipe.getLikeCount(),CommentNum,recipe.getName());


            topLikedRecipi.add(topRecipes);
        }

        return topLikedRecipi;
    }

    // 오늘의 레시피 랜덤 띄우기
    public List<CommunityTodayRecipeDTO> getTodayRecipe(){
        List<CommunityTodayRecipeDTO> RandomRecipe = new ArrayList<>();
        List<Recipe> rendomRecipeRepo = recipeRepository.findRandomRecipes();

        for(int i=0; i<Math.min(rendomRecipeRepo.size(),4); i++){
            CommunityTodayRecipeDTO communityTodayRecipeDTO = new CommunityTodayRecipeDTO(rendomRecipeRepo.get(i).getRecipeId(),
                    rendomRecipeRepo.get(i).getFoodImage(),rendomRecipeRepo.get(i).getName());

            RandomRecipe.add(communityTodayRecipeDTO);
        }
        return RandomRecipe;
    }

    // 레시피 하나 띄우기
    public CommunityDetailDTO getDetail(Long recipeId){

       Recipe recipe = recipeRepository.findByRecipeId(recipeId);

       // 해당 레시피가 가지는 전체 코멘트 수
       int commentCount = commentsRepository.countByRecipeRecipeId(recipeId);

       // 해당 레시피가 가지는 comment
        List<CommentDTO> comments = commentsRepository.getCommentLately(recipeId);

        // 리스트에서 최신 2개만 가져오기
        List<CommentDTO> latestComments = comments.stream()
                .limit(2)
                .collect(Collectors.toList());


        // 맛 List 뽑기
        List<String> flavorList = new ArrayList<>();
        for(int i=0; i<recipe.getFoodFlavors().size(); i++)
        {
            flavorList.add(recipe.getFoodFlavors().get(i).getTaste());
        }
        return CommunityDetailDTO.builder()
                .recipeId(recipe.getRecipeId())
                .foodImage(recipe.getFoodImage())
                .level(recipe.getLevel())
                .time(recipe.getTime())
                .postDate(recipe.getPostDate())
                .userId(recipe.getUser().getUserId())
                .profilePic(recipe.getUser().getProfilePic())
                .name(recipe.getName())
                .spicyLevel(recipe.getSpicyLevel())
                .isHart(recipe.getLikes().isEmpty())
                .isScrap(recipe.getScraps().isEmpty())
                .crapCount(recipe.getScrapCount())
                .likeCount(recipe.getLikeCount())
                .commentCount(commentCount)
                .information(recipe.getInformation())
                .commentDTOList(latestComments)
                .flavor(flavorList)
                .build();

    }
}
