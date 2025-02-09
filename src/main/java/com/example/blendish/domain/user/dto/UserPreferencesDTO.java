package com.example.blendish.domain.user.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserPreferencesDTO {
    private String userId;
    private String country;
    private String hometown;
    private List<TastePreferenceDTO> tastePreferences;
}