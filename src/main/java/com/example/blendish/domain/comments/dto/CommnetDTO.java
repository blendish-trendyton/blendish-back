package com.example.blendish.domain.comments.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class CommnetDTO {
    private String userId;
    private String profilePic;
    private String content;
    private Date createdAt;
    private int numOfReply;
}
