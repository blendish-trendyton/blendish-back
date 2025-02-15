package com.example.blendish.domain.recipe.service;

import com.example.blendish.domain.comments.repository.CommentsRepository;
import com.example.blendish.domain.recipe.dto.RecipeListDTO;
import com.example.blendish.domain.recipe.entity.Category;
import com.example.blendish.domain.recipe.entity.Recipe;
import com.example.blendish.domain.recipe.repository.CategoryRepository;
import com.example.blendish.domain.recipe.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final CategoryRepository categoryRepository;
    private final RecipeRepository recipeRepository;
    private final CommentsRepository commentsRepository;
    // 레시피 검색결과
    public List<Category> getCategoryByName(String name){
       return categoryRepository.findByNameContaining(name);
    }

    // 해당카테고리에 있는 레시피들
    public List<RecipeListDTO> getRecipeList(String name){
        List<RecipeListDTO> recipeLists = new ArrayList<>();
        List<Recipe> recipes = recipeRepository.findRecipeByNameContaining(name);

        for(int i=0; i<recipes.size(); i++){
            RecipeListDTO recipeList = new RecipeListDTO().builder().recipeId(recipes.get(i).getRecipeId())
                    .foodImage(recipes.get(i).getFoodImage())
                    .name(recipes.get(i).getName())
                    .information(recipes.get(i).getInformation())
                    .likeCount(recipes.get(i).getLikeCount())
                    .commentCount(commentsRepository.countByRecipeRecipeId(recipes.get(i).getRecipeId())).build();
        }

        return  recipeLists;
    }
}
