package com.example.blendish.domain.recipe.service;

import com.example.blendish.domain.recipe.dto.AddRecipeDTO;
import com.example.blendish.domain.recipe.entity.Recipe;
import com.example.blendish.domain.recipe.entity.RecipeSteps;
import com.example.blendish.domain.recipe.repository.RecipeRepository;
import com.example.blendish.domain.user.entity.User;
import com.example.blendish.domain.user.repository.UserRepository;
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

    public void createRecipe(AddRecipeDTO addRecipeDTO, String userId) {
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

        Recipe recipe = Recipe.builder()
                .name(addRecipeDTO.name())
                .level(addRecipeDTO.level())
                .ingredients(addRecipeDTO.ingredients())
                .information(addRecipeDTO.information())
                .foodImage(addRecipeDTO.foodImage())
                .user(user)
                .steps(steps)
                .build();

        steps.forEach(step -> step.updateRecipe(recipe));

        recipeRepository.save(recipe);
    }

}
