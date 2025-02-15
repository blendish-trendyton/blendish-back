package com.example.blendish.domain.recipe.service;

import com.example.blendish.domain.recipe.dto.AddRecipeDTO;
import com.example.blendish.domain.recipe.entity.Ingredient;
import com.example.blendish.domain.recipe.entity.Recipe;
import com.example.blendish.domain.recipe.entity.RecipeSteps;
import com.example.blendish.domain.recipe.repository.RecipeRepository;
import com.example.blendish.domain.user.entity.User;
import com.example.blendish.domain.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    public void createUserRecipe(AddRecipeDTO addRecipeDTO, String userId, String imageUrl) {
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new IllegalArgumentException("유저를 찾을 수 없습니다.");
        }

        List<RecipeSteps> steps = addRecipeDTO.steps().stream()
                .map(stepDTO -> RecipeSteps.builder()
                        .stepNum(stepDTO.stepNumber())
                        .details(stepDTO.details())
                        .build())
                .collect(Collectors.toList());

        List<Ingredient> ingredients = addRecipeDTO.ingredients();

        Recipe recipe = Recipe.builder()
                .name(addRecipeDTO.name())
                .level(addRecipeDTO.level())
                .ingredients(ingredients)
                .information(addRecipeDTO.information())
                .foodImage(imageUrl)
                .user(user)
                .isAiGenerated(false)
                .steps(steps)
                .build();

        steps.forEach(step -> step.updateRecipe(recipe));

        recipeRepository.save(recipe);
    }

    public void createAiRecipe(AddRecipeDTO addRecipeDTO,String userId) {
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new IllegalArgumentException("유저를 찾을 수 없습니다.");
        }

        List<RecipeSteps> steps = addRecipeDTO.steps().stream()
                .map(stepDTO -> RecipeSteps.builder()
                        .stepNum(stepDTO.stepNumber())
                        .details(stepDTO.details())
                        .build())
                .collect(Collectors.toList());

        List<Ingredient> ingredients = addRecipeDTO.ingredients();

        Recipe recipe = Recipe.builder()
                .name(addRecipeDTO.name())
                .level(addRecipeDTO.level())
                .ingredients(ingredients)
                .information(addRecipeDTO.information())
                .foodImage(null)
                .user(null)
                .isAiGenerated(true)
                .steps(steps)
                .build();

        steps.forEach(step -> step.updateRecipe(recipe));

        recipeRepository.save(recipe);
    }

}
