package com.example.blendish.domain.recipe.repository;

import com.example.blendish.domain.recipe.entity.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScrapRepository extends JpaRepository<Scrap,Long> {

//    void deleteByRecipeRecipeIdAndUserId(Long recipeId,Long userId);
}
