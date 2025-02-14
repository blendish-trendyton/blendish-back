package com.example.blendish.domain.recipe.service;

import com.example.blendish.domain.recipe.entity.Category;
import com.example.blendish.domain.recipe.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final CategoryRepository categoryRepository;

    // 레시피 검색결과
    public List<Category> getCategoryByName(String name){
       return categoryRepository.findByNameContaining(name);
    }

}
