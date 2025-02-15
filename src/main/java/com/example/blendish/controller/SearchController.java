package com.example.blendish.controller;
import com.example.blendish.domain.recipe.entity.Category;
import com.example.blendish.domain.recipe.service.SearchService;
import com.example.blendish.global.dto.ApiResponseTemplate;
import com.example.blendish.global.response.SuccessCode;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Search Controller", description = "검색 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/Search")
public class SearchController {

    private final SearchService searchService;

    // 카테고리 검색
    @GetMapping("/searchCategories")
    public ResponseEntity<ApiResponseTemplate<?>> searchCategories(@RequestParam(name = "name") String name) {
        List<Category> category= searchService.getCategoryByName(name);

        Map<String, Object> response = new HashMap<>();
        response.put("categories", category);
        response.put("totalCount", category.size());

        return ResponseEntity.ok(ApiResponseTemplate.success(SuccessCode.OK, response));
    }
}
