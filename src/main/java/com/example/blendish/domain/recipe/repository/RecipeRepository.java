package com.example.blendish.domain.recipe.repository;

import com.example.blendish.domain.recipe.dto.CommunityHotRecipyDTO;
import com.example.blendish.domain.recipe.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Recipe findByRecipeId(Long recipiId);
}
