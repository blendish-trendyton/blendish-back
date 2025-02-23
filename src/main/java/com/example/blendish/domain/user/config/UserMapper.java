package com.example.blendish.domain.user.config;

import com.example.blendish.domain.user.dto.preference.TastePreferenceDTO;
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
        dto.setRole(user.getRole());

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

    public static User toEntity(UserDTO dto) {
        if (dto == null) return null;

        User user = new User();
        user.setUserId(dto.getUserId());
        user.setUserPw(dto.getUserPw());
        user.setEmail(dto.getEmail());
        user.setHometown(dto.getHometown());
        user.setCountry(dto.getCountry());
        user.setProfilePic(dto.getProfilePic());
        user.setRole(dto.getRole());

        if (dto.getTastePreference() != null) {
            List<TastePreference> tastePreferences = dto.getTastePreference().stream()
                    .map(tpDTO -> {
                        TastePreference tastePreference = new TastePreference();
                        tastePreference.setTaste(tpDTO.getTaste());
                        tastePreference.setSpicyLevel(tpDTO.getSpicyLevel());
                        tastePreference.setUser(user);
                        return tastePreference;
                    })
                    .collect(Collectors.toList());
            user.setTastePreferences(tastePreferences);
        }

        return user;
    }

    private static TastePreferenceDTO mapTastePreference(TastePreference tastePreference) {
        TastePreferenceDTO dto = new TastePreferenceDTO();
        dto.setTaste(tastePreference.getTaste());
        dto.setSpicyLevel(tastePreference.getSpicyLevel());
        return dto;
    }
}
