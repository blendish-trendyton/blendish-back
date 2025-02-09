package com.example.blendish.domain.user.dto;

import lombok.Data;

@Data
public class JoinDTO {
    private String userId;
    private String email;
    private String userPw;
}
