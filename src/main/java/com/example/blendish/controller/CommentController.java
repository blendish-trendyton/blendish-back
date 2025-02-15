package com.example.blendish.controller;

import com.example.blendish.domain.comments.dto.CommentAllDTO;
import com.example.blendish.domain.comments.dto.CommentDTO;
import com.example.blendish.domain.comments.service.CommentService;
import com.example.blendish.global.dto.ApiResponseTemplate;
import com.example.blendish.global.response.SuccessCode;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Comment Controller", description = "댓글 API")
@RestController
@RequestMapping("/api/Comment")
@AllArgsConstructor
public class CommentController {
    private final CommentService commentService;

    //부모댓글 전체 띄우기
    @GetMapping("/ParentsComment")
    public ResponseEntity<ApiResponseTemplate<List<CommentDTO>>> getParentsComment(@RequestParam(name = "recipeId") Long recipeId) {

        List<CommentDTO> commentDTOList = commentService.getParentCommnet(recipeId);
        return ResponseEntity.ok(ApiResponseTemplate.success(SuccessCode.OK, commentDTOList));
    }

    // 전체 댓글 띄우기
    @GetMapping("/AllComment")
    public ResponseEntity<ApiResponseTemplate<List<CommentAllDTO>>> getAllComment(@RequestParam(name = "recipeId") Long recipeId) {

        List<CommentAllDTO> commentAllDTOList = commentService.getAllComment(recipeId);
        return ResponseEntity.ok(ApiResponseTemplate.success(SuccessCode.OK, commentAllDTOList));
    }
}
