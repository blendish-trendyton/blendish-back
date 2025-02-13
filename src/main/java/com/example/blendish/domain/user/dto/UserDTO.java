package com.example.blendish.domain.user.dto;

import com.example.blendish.domain.user.dto.preference.TastePreferenceDTO;
import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
    private String userId;

    private String userPw,email,hometown,
            country,profilePic;
    private String role;
    private List<TastePreferenceDTO> tastePreference;
}
