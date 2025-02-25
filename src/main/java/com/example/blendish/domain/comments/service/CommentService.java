package com.example.blendish.domain.comments.service;

import com.example.blendish.domain.comments.dto.CommentAllDTO;
import com.example.blendish.domain.comments.dto.CommentDTO;
import com.example.blendish.domain.comments.dto.WriteCommentDTO;
import com.example.blendish.domain.comments.entity.Comment;
import com.example.blendish.domain.comments.repository.CommentsRepository;
import com.example.blendish.domain.recipe.entity.Recipe;
import com.example.blendish.domain.recipe.repository.RecipeRepository;
import com.example.blendish.domain.user.dto.CustomUserDetails;
import com.example.blendish.domain.user.entity.User;
import com.example.blendish.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@Service
@AllArgsConstructor
@Slf4j
public class CommentService {
    private final CommentsRepository commentsRepository;
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;

    // 부모 댓글띄우기
    public List<CommentDTO> getParentCommnet(Long recipeId){
       return commentsRepository.getCommentLately(recipeId);
    }

    public List<CommentAllDTO> getAllComment(Long recipeId) {
        List<Comment> comments = commentsRepository.getCommentsByRecipeRecipeId(recipeId);

        List<CommentAllDTO> commentDTOList = new ArrayList<>();

        for (Comment comment : comments) {
            // 부모 댓글을 DTO로 변환
            CommentAllDTO commentDTO = CommentAllDTO.builder()
                    .commentId(comment.getCommentId())
                    .createdAt(comment.getCreatedAt())
                    .content(comment.getContent())
                    .profilePic(comment.getUser().getProfilePic())
                    .userId(comment.getUser().getUserId())
                    .ReplyList(getChildComments(comment.getReplies()))
                    .build();

            commentDTOList.add(commentDTO);
        }

        return commentDTOList;
    }

    // 대댓글을 재귀적으로 가져오는 메서드
    public List<CommentDTO> getChildComments(List<Comment> childs) {
        List<CommentDTO> childDTOList = new ArrayList<>();

        for (int i=0; i<childs.size(); i++) {
            CommentDTO childDTO = CommentDTO.builder()
                    .commentId(childs.get(i).getCommentId())
                    .createdAt(childs.get(i).getCreatedAt())
                    .content(childs.get(i).getContent())
                    .profilePic(childs.get(i).getUser().getProfilePic())
                    .userId(childs.get(i).getUser().getUserId())
                    .build();

            childDTOList.add(childDTO);
        }

        return childDTOList;
    }

    //댓글 작성
    public void insertComment(WriteCommentDTO writeCommentDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = userRepository.findByUserId(userDetails.getUsername());

            log.info(String.valueOf(writeCommentDTO.getParentCommentId()));

            // 부모객체가 null인지 확인
            Comment parentComment = null;
            if (writeCommentDTO.getParentCommentId() != null) {
                parentComment = commentsRepository.findByCommentId(writeCommentDTO.getParentCommentId());
            }


            //레시피 객체
            Recipe recipe = recipeRepository.findByRecipeId(writeCommentDTO.getRecipeId());

            Comment comment = new Comment();
            comment.setParentComment(parentComment);
            comment.setUser(user);
            comment.setRecipe(recipe);
            comment.setContent(writeCommentDTO.getContent());
            comment.setParentCommentId(writeCommentDTO.getParentCommentId());
            commentsRepository.save(comment);

        }
    }
}
