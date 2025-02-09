package com.example.blendish.domain.user.config;

import com.example.blendish.domain.user.dto.TastePreferenceDTO;
import com.example.blendish.domain.user.dto.UserDTO;
import com.example.blendish.domain.user.entity.TastePreference;
import com.example.blendish.domain.user.entity.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {
    public static UserDTO toDTO(User user) {
        if (user == null) return null;

        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setUserPw(user.getUserPw());
        dto.setEmail(user.getEmail());
        dto.setHometown(user.getHometown());
        dto.setCountry(user.getCountry());
        dto.setProfilePic(user.getProfilePic());

        List<TastePreferenceDTO> tastePreferenceDTOList = null;
        if (user.getTastePreferences() != null) {
            tastePreferenceDTOList = user.getTastePreferences()
                    .stream()
                    .map(UserMapper::mapTastePreference)
                    .collect(Collectors.toList());
        }
        dto.setTastePreference(tastePreferenceDTOList);

        return dto;
    }

    private static TastePreferenceDTO mapTastePreference(TastePreference tastePreference) {
        TastePreferenceDTO dto = new TastePreferenceDTO();
        dto.setTaste(tastePreference.getTaste());
        dto.setSpicyLevel(tastePreference.getSpicyLevel());
        return dto;
    }
}
