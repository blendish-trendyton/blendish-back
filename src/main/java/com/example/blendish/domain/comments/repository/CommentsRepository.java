package com.example.blendish.domain.comments.repository;

import com.example.blendish.domain.comments.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentsRepository extends JpaRepository<Comment,Long> {
    // 해당 recipeId에 대한 댓글 개수 카운트
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.recipe.recipeId = :recipeId")
    int countByRecipeRecipeId(@Param("recipeId") Long recipeId);
}
