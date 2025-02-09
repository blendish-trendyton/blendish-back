package com.example.blendish.domain.user.service;

import com.example.blendish.domain.user.config.UserMapper;
import com.example.blendish.domain.user.dto.JoinDTO;
import com.example.blendish.domain.user.dto.UserDTO;
import com.example.blendish.domain.user.dto.UserPreferencesDTO;
import com.example.blendish.domain.user.entity.TastePreference;
import com.example.blendish.domain.user.entity.User;
import com.example.blendish.domain.user.repository.TastePreferenceRepository;
import com.example.blendish.domain.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JoinService {
    private final UserRepository userRepository;
    private final TastePreferenceRepository tastePreferenceRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public JoinService(UserRepository userRepository, TastePreferenceRepository tastePreferenceRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {

        this.userRepository = userRepository;
        this.tastePreferenceRepository = tastePreferenceRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public boolean checkUserId(String userId) {
        return userRepository.existsByUserId(userId);
    }

    public User joinProcess(JoinDTO joinDTO) {
            String userId = String.valueOf(joinDTO.getUserId());
            String email = joinDTO.getEmail();
            String userPw = joinDTO.getUserPw();

            User data = new User();

            data.setUserId(userId);
            data.setEmail(email);
            data.setUserPw(bCryptPasswordEncoder.encode(userPw));
            data.setRole("ROLE_A");

            return userRepository.save(data);

    }

    public UserDTO updateUserPreferences(UserPreferencesDTO dto) {
        Optional<User> optionalUser = userRepository.findByUserId(dto.getUserId());

        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("해당 유저를 찾을 수 없습니다.");
        }

        User user = optionalUser.get();

        user.setCountry(dto.getCountry());
        user.setHometown(dto.getHometown());
        user.setRole("ROLE_ADMIN");
        userRepository.save(user);

        tastePreferenceRepository.deleteByUser(user);

        List<TastePreference> preferences = dto.getTastePreferences().stream()
                .map(tasteDTO -> {
                    TastePreference tastePreference = new TastePreference();
                    tastePreference.setUser(user);
                    tastePreference.setTaste(tasteDTO.getTaste());
                    tastePreference.setSpicyLevel(tasteDTO.getSpicyLevel());
                    return tastePreference;
                })
                .collect(Collectors.toList());

        tastePreferenceRepository.saveAll(preferences);

        return UserMapper.toDTO(user);
    }


}
