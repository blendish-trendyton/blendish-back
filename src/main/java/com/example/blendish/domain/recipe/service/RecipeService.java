package com.example.blendish.domain.recipe.service;

import com.example.blendish.domain.recipe.dto.AddAiRecipeDTO;
import com.example.blendish.domain.recipe.dto.AddUserRecipeDTO;
import com.example.blendish.domain.recipe.entity.*;
import com.example.blendish.domain.recipe.repository.RecipeRepository;
import com.example.blendish.domain.user.entity.User;
import com.example.blendish.domain.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createUserRecipe(AddUserRecipeDTO addRecipeDTO, String userId, String imageUrl) {
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new IllegalArgumentException("유저를 찾을 수 없습니다.");
        }

        Recipe recipe = Recipe.builder()
                .name(addRecipeDTO.name())
                .level(addRecipeDTO.level())
                .time(addRecipeDTO.time())
                .information(addRecipeDTO.information())
                .foodImage(imageUrl)
                .user(user)
                .isAiGenerated(false)
                .ingredients(new ArrayList<>())
                .steps(new ArrayList<>())
                .build();

        recipeRepository.save(recipe);

        List<RecipeSteps> steps = addRecipeDTO.steps().stream()
                .map(stepDTO -> RecipeSteps.builder()
                        .stepNum(stepDTO.stepNumber())
                        .details(stepDTO.details())
                        .recipe(recipe)
                        .build())
                .collect(Collectors.toList());

        List<Ingredient> ingredients = addRecipeDTO.ingredients().stream()
                .map(ingredient -> Ingredient.builder()
                        .name(ingredient.getName())
                        .amount(ingredient.getAmount())
                        .recipe(recipe)
                        .build())
                .toList();

        recipe.getSteps().addAll(steps);
        recipe.getIngredients().addAll(ingredients);

        recipeRepository.save(recipe);
    }


    @Transactional
    public void createAiRecipe(AddAiRecipeDTO addRecipeDTO, String userId) {
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new IllegalArgumentException("유저를 찾을 수 없습니다.");
        }

        Recipe recipe = Recipe.builder()
                .name(addRecipeDTO.name())
                .level(addRecipeDTO.level())
                .time(addRecipeDTO.time())
                .information(addRecipeDTO.information())
                .foodImage(null)
                .user(user)
                .isAiGenerated(true)
                .aiSteps(new ArrayList<>())
                .build();

        recipeRepository.save(recipe);

        List<AiRecipeSteps> steps = addRecipeDTO.steps().stream()
                .map(stepDTO -> AiRecipeSteps.builder()
                        .stepNum(stepDTO.stepNumber())
                        .details(stepDTO.details())
                        .recipe(recipe)
                        .build())
                .toList();

        recipe.getAiSteps().addAll(steps);

        recipeRepository.save(recipe);
    }
}
