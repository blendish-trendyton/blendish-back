package com.example.blendish.domain.comments.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long commentId;
    private String userId;
    private String profilePic;
    private String content;
    private Date createdAt;
    private Long numOfReply;
}
