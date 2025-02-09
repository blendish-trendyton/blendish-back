package com.example.blendish.domain.user.dto;

import com.example.blendish.domain.user.entity.TastePreference;
import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
    private String userId;

    private String userPw,email,hometown,
            country,profilePic;
    private List<TastePreferenceDTO> tastePreference;
}
