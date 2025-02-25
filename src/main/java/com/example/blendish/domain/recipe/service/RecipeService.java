package com.example.blendish.domain.recipe.service;

import com.example.blendish.domain.recipe.dto.AddAiRecipeDTO;
import com.example.blendish.domain.recipe.dto.AddUserRecipeDTO;
import com.example.blendish.domain.recipe.entity.*;
import com.example.blendish.domain.recipe.repository.RecipeRepository;
import com.example.blendish.domain.user.entity.User;
import com.example.blendish.domain.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final RecipeImageService recipeImageService;
    private final RecipeMapper recipeMapper;
    private final ObjectMapper objectMapper;

    @Transactional
    public void createUserRecipe(String addRecipeDTOJson, String userId, MultipartFile image, List<MultipartFile> stepImages) throws IOException {
        AddUserRecipeDTO addRecipeDTO = objectMapper.readValue(addRecipeDTOJson, AddUserRecipeDTO.class);

        String imageUrl = recipeImageService.uploadImage(image);
        List<String> stepImageUrls = recipeImageService.uploadStepImages(stepImages);

        saveRecipe(addRecipeDTO, userId, imageUrl, stepImageUrls);
    }

    @Transactional
    protected void saveRecipe(AddUserRecipeDTO addRecipeDTO, String userId, String imageUrl, List<String> stepImageUrls) {
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
                .likeCount(0)
                .isAiGenerated(false)
                .ingredients(new ArrayList<>())
                .steps(new ArrayList<>())
                .build();

        recipeRepository.save(recipe);

        List<RecipeSteps> steps = recipeMapper.mapToRecipeSteps(addRecipeDTO.steps(), stepImageUrls, recipe);
        List<Ingredient> ingredients = recipeMapper.mapToIngredients(addRecipeDTO.ingredients(), recipe);

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
                .likeCount(0)
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

