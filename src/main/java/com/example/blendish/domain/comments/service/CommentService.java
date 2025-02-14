package com.example.blendish.domain.comments.service;

import com.example.blendish.domain.comments.dto.CommentAllDTO;
import com.example.blendish.domain.comments.dto.CommentDTO;
import com.example.blendish.domain.comments.entity.Comment;
import com.example.blendish.domain.comments.repository.CommentsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CommentService {
    private final CommentsRepository commentsRepository;

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
    private List<CommentDTO> getChildComments(List<Comment> childs) {
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

}
