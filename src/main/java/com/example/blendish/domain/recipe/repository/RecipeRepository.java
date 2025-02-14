package com.example.blendish.domain.recipe.repository;

import com.example.blendish.domain.recipe.entity.Recipe;
import com.example.blendish.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Recipe findByRecipeId(Long recipiId);

    @Query("SELECT r FROM Recipe r ORDER BY FUNCTION('RAND')")  // 랜덤으로 레시피 정렬
    List<Recipe> findRandomRecipes();

    List<Recipe> findByUser(User user);

    // like 증가
//    @Modifying
//    @Query("UPDATE Recipe r SET r.likeCount = r.likeCount + 1 WHERE r.recipeId = :recipeId")
//    void incrementLikeCount(Long recipeId);
//
//    // like 감소
//    @Modifying
//    @Query("UPDATE Recipe r SET r.likeCount = r.likeCount - 1 WHERE r.recipeId = :recipeId")
//    void decrementLikeCount(Long recipeId);
//
//    // 스크랩 증가
//    @Modifying
//    @Query("UPDATE Recipe r SET r.scrapCount = r.scrapCount + 1 WHERE r.recipeId = :recipeId")
//    void incrementScrapCount(Long recipeId);
//
//    @Modifying
//    @Query("UPDATE Recipe r SET r.scrapCount = r.likeCount - 1 WHERE r.recipeId = :recipeId")
//    void decrementScrapCount(Long recipeId);

}
