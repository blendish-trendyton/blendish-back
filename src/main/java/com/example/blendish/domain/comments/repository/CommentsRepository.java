package com.example.blendish.domain.comments.repository;

import com.example.blendish.domain.comments.dto.CommentDTO;
import com.example.blendish.domain.comments.entity.Comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface CommentsRepository extends JpaRepository<Comment,Long> {
    // 해당 recipeId에 대한 댓글 개수 카운트
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.recipe.recipeId = :recipeId")
    int countByRecipeRecipeId(@Param("recipeId") Long recipeId);

    // 해당 게시글에 대한 댓글
    @Query(value = "SELECT new com.example.blendish.domain.comments.dto.CommentDTO(c.commentId, c.user.userId, c.user.profilePic, c.content, c.createdAt, " +
            "(SELECT COUNT(r) as numOfReply FROM Comment r WHERE r.parentComment.commentId = c.commentId)) " +
            "FROM Comment c WHERE c.recipe.recipeId = :recipeId AND c.parentComment IS NULL " +
            "ORDER BY c.createdAt DESC ")
    List<CommentDTO> getCommentLately(@Param("recipeId") Long recipeId);



}
