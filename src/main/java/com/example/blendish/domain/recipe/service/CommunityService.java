package com.example.blendish.domain.recipe.service;

import com.example.blendish.domain.comments.dto.CommentDTO;
import com.example.blendish.domain.comments.repository.CommentsRepository;
import com.example.blendish.domain.recipe.dto.*;
import com.example.blendish.domain.recipe.entity.Likes;
import com.example.blendish.domain.recipe.entity.Recipe;
import com.example.blendish.domain.recipe.entity.RecipeSteps;
import com.example.blendish.domain.recipe.entity.Scrap;
import com.example.blendish.domain.recipe.repository.LikeRepository;
import com.example.blendish.domain.recipe.repository.RecipeRepository;
import com.example.blendish.domain.recipe.repository.ScrapRepository;
import com.example.blendish.domain.recipesteps.dto.RecipeStepsDTO;
import com.example.blendish.domain.recipesteps.repository.RecipestepsRepository;
import com.example.blendish.domain.user.dto.CustomUserDetails;
import com.example.blendish.domain.user.entity.User;
import com.example.blendish.domain.user.repository.UserRepository;
import io.jsonwebtoken.lang.Collections;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.geom.FlatteningPathIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class CommunityService {
    private final RecipeRepository recipeRepository;
    private final LikeRepository likeRepository;
    private final CommentsRepository commentsRepository;
    private final ScrapRepository scrapRepository;
    private final RecipestepsRepository recipestepsRepository;
    private final UserRepository userRepository;

    // 인기 레시피 가져오는 서비스
    public List<CommunityHotRecipeDTO> getTopLikedRecipes() {

        List<CommunityHotRecipeDTO> topLikedRecipi = new ArrayList<>();

        // 좋아요 많은 id 가져오기
        List<Long> topLikedRecipeIds = likeRepository.findTopLikedRecipeIds();


        if (topLikedRecipeIds.isEmpty()) {
            return Collections.emptyList();
        }

        // 가져온거 4번 돌리면서 dto 에 집어넣기
        for (int i = 0; i < Math.min(topLikedRecipeIds.size(), 4); i++) {
            Long recipeId = topLikedRecipeIds.get(i);
            // 레시피 객체 가져오기
            Recipe recipe = recipeRepository.findByRecipeId(recipeId);
            // 코멘트 개수 가져오기
            int CommentNum = commentsRepository.countByRecipeRecipeId(recipeId);

            CommunityHotRecipeDTO topRecipes = new CommunityHotRecipeDTO(recipeId, recipe.getFoodImage(), recipe.getLikeCount(), CommentNum, recipe.getName());


            topLikedRecipi.add(topRecipes);
        }

        return topLikedRecipi;
    }

    // 오늘의 레시피 랜덤 띄우기
    public List<CommunityTodayRecipeDTO> getTodayRecipe() {
        List<CommunityTodayRecipeDTO> RandomRecipe = new ArrayList<>();
        List<Recipe> rendomRecipeRepo = recipeRepository.findRandomRecipes();

        for (int i = 0; i < Math.min(rendomRecipeRepo.size(), 4); i++) {
            CommunityTodayRecipeDTO communityTodayRecipeDTO = new CommunityTodayRecipeDTO(rendomRecipeRepo.get(i).getRecipeId(),
                    rendomRecipeRepo.get(i).getFoodImage(), rendomRecipeRepo.get(i).getName());

            RandomRecipe.add(communityTodayRecipeDTO);
        }
        return RandomRecipe;
    }

    // 레시피 하나 띄우기
    public CommunityDetailDTO getDetail(Long recipeId) {

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
        for (int i = 0; i < recipe.getFoodFlavors().size(); i++) {
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

    // 이미 좋아요한 like 인지 찾기
    public boolean lsHaveLike(Long recipeId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = userRepository.findByUserId(userDetails.getUsername());


            log.info(user.getEmail());


            boolean isHave = likeRepository.isLike( user.getId(),recipeId);

            log.info(String.valueOf(isHave));

            return isHave;
        } else {
            log.warn("Authentication null or 인증오류");

            return false;
        }
    }

    // 좋아요 클릭시
    @Transactional
    public void insertLike(Long recipeId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = userRepository.findByUserId(userDetails.getUsername());


            // 사용자 이름 출력
            log.info("Username: " + user.getEmail());


            // Recipe 조회
            Recipe recipe = recipeRepository.findByRecipeId(recipeId);

            // Like 객체 생성
            Likes like = new Likes();
            like.setUser(user);
            like.setRecipe(recipe);

            // Likes 저장
            likeRepository.save(like);

            // 레시피의 likecount 증가
            recipeRepository.incrementLikeCount(recipeId);
        }

}

    //   좋아요 삭제시
    @Transactional
    public void removeLike(Long recipeId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            // 유저 객체
            User user = userRepository.findByUserId(userDetails.getUsername());

            // Likes 삭제
            likeRepository.deleteByRecipeRecipeIdAndUserId(recipeId,user.getId());

            // 레시피의 likecount 감소
            recipeRepository.decrementLikeCount(recipeId);
        }
    }

    //  이미 있는 스크랩인지 확인
    @Transactional
    public boolean isScrap(Long recipeId){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = userRepository.findByUserId(userDetails.getUsername());


            log.info(user.getEmail());


            boolean isHave = scrapRepository.isScrap( user.getId(),recipeId);

            log.info(String.valueOf(isHave));

            return isHave;

        } else {
            log.warn("Authentication null or 인증오류");

            return false;
        }

    }

    //   스크랩 등록시
    @Transactional
    public void insertScrap(Long recipeId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            // 유저 객체
            User user = userRepository.findByUserId(userDetails.getUsername());

            // Recipe 조회
            Recipe recipe = recipeRepository.findByRecipeId(recipeId);

            // 스크랩 객체 생성
            Scrap scrap = new Scrap();
            scrap.setUser(user);
            scrap.setRecipe(recipe);

            // 스크랩 저장
            scrapRepository.save(scrap);

            // 레시피의 scrapCount 증가
            recipeRepository.incrementScrapCount(recipeId);
        }
    }

    // 스크랩 삭제시
    @Transactional
    public void removeScrap(Long recipeId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            // 유저 객체
            User user = userRepository.findByUserId(userDetails.getUsername());

            // 스크랩 삭제
            scrapRepository.deleteByRecipeRecipeIdAndUserId(recipeId,user.getId());

            // 레시피의 스크랩 감소
            recipeRepository.decrementScrapCount(recipeId);
        }
    }

    // 레시피 디테일 띄우기
    public RecipeDetailDTO getAllDetail(Long recipeId){

        Recipe recipe = recipeRepository.findByRecipeId(recipeId);
        RecipeSteps recipeSteps =recipestepsRepository.findByRecipeRecipeId(recipeId);

        RecipeStepsDTO recipeStepsDTO = new RecipeStepsDTO(recipeSteps.getDetails(),recipeSteps.getStepImage(),
                recipeSteps.getStepNum());


        return RecipeDetailDTO.builder()
                .recipeId(recipeId)
                .time(recipe.getTime())
                .level(recipe.getLevel())
                .name(recipe.getName())
                .ingredients(recipe.getIngredients())
                .recipeSteps(recipeStepsDTO)
                .build();

    }

    // 좋아요 상위 10 개 레시피 뽑기
    public List<LiketenRecipeDTO> getTenRecipe(){
        Pageable pageable = PageRequest.of(0, 10);
        return likeRepository.findTopTenLikedRecipeIds(pageable);
    }


}
