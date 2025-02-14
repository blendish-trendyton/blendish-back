package com.example.blendish.domain.comments.dto;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentAllDTO {
    private Long commentId;
    private String userId;
    private String profilePic;
    private String content;
    private Date createdAt;
    private List<CommentDTO> ReplyList;
}
