package com.example.blendish.domain.recipe.repository;

import com.example.blendish.domain.recipe.dto.LiketenRecipeDTO;
import com.example.blendish.domain.recipe.entity.Likes;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LikeRepository extends JpaRepository<Likes, Long> {
    @Query("SELECT l.recipe.recipeId FROM Likes l GROUP BY l.recipe.recipeId ORDER BY COUNT(l) DESC")
    List<Long> findTopLikedRecipeIds();

    void deleteByRecipeRecipeIdAndUserId(Long recipeId,Long userId);

    @Query("SELECT new com.example.blendish.domain.recipe.dto.LiketenRecipeDTO(l.recipe.recipeId, l.recipe.name) " +
            "FROM Likes l " +
            "GROUP BY l.recipe.recipeId " +
            "ORDER BY COUNT(l) DESC")
    List<LiketenRecipeDTO> findTopTenLikedRecipeIds(Pageable pageable);

    @Query("SELECT COUNT(l) > 0 FROM Likes l WHERE l.user.id = :id AND l.recipe.recipeId = :recipeId")
    boolean isLike(@Param("id") Long id, @Param("recipeId") Long recipeId);



}
