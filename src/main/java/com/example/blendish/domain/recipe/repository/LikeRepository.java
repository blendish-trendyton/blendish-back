package com.example.blendish.domain.recipe.repository;

import com.example.blendish.domain.recipe.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LikeRepository extends JpaRepository<Likes, Long> {
    @Query("SELECT l.recipe.recipeId FROM Likes l GROUP BY l.recipe.recipeId ORDER BY COUNT(l) DESC")
    List<Long> findTopLikedRecipeIds();

    void deleteByRecipeRecipeIdAndUserId(Long recipeId,Long userId);
}
