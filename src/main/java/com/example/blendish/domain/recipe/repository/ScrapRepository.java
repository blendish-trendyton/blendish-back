package com.example.blendish.domain.recipe.repository;

import com.example.blendish.domain.recipe.entity.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScrapRepository extends JpaRepository<Scrap,Long> {

    void deleteByRecipeRecipeIdAndUserId(Long recipeId,Long userId);

    @Query("SELECT COUNT(s) > 0 FROM Scrap s WHERE s.user.id = :id AND s.recipe.recipeId = :recipeId")
    boolean isScrap(@Param("id") Long id, @Param("recipeId") Long recipeId);

}
