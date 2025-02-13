package com.example.blendish.domain.recipe.repository;

import com.example.blendish.domain.recipe.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Recipe findByRecipeId(Long recipiId);

    @Query("SELECT r FROM Recipe r ORDER BY FUNCTION('RAND')")  // 랜덤으로 레시피 정렬
    List<Recipe> findRandomRecipes();
}
