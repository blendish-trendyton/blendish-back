package com.example.blendish.domain.comments.service;

import com.example.blendish.domain.comments.dto.CommentDTO;
import com.example.blendish.domain.comments.repository.CommentsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CommentService {
    private final CommentsRepository commentsRepository;

    public List<CommentDTO> getParentCommnet(Long recipeId){
       return commentsRepository.getCommentLately(recipeId);
    }
}
