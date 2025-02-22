package com.example.blendish.domain.comments.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WriteCommentDTO {
    private Long recipeId;
    private Long parentCommentId;
    private String content;
}
