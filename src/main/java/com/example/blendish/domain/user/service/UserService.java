package com.example.blendish.domain.user.service;

import com.example.blendish.domain.recipe.dto.RecipeSummaryDTO;
import com.example.blendish.domain.recipe.entity.Recipe;
import com.example.blendish.domain.recipe.repository.RecipeRepository;
import com.example.blendish.domain.recipe.repository.ScrapRepository;
import com.example.blendish.domain.user.config.UserMapper;
import com.example.blendish.domain.user.dto.UserDTO;
import com.example.blendish.domain.user.dto.preference.TastePreferenceDTO;
import com.example.blendish.domain.user.dto.preference.UserDetailDTO;
import com.example.blendish.domain.user.entity.TastePreference;
import com.example.blendish.domain.user.entity.User;
import com.example.blendish.domain.user.repository.TastePreferenceRepository;
import com.example.blendish.domain.user.repository.UserRepository;
import com.example.blendish.global.dto.ApiResponseTemplate;
import com.example.blendish.global.s3.S3UploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final ScrapRepository scrapRepository;
    private final S3UploadService s3UploadService;
    private final TastePreferenceRepository tastePreferenceRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, RecipeRepository recipeRepository, ScrapRepository scrapRepository, S3UploadService s3UploadService, TastePreferenceRepository tastePreferenceRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
        this.scrapRepository = scrapRepository;
        this.s3UploadService = s3UploadService;
        this.tastePreferenceRepository = tastePreferenceRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public List<UserDTO> getAllUsers() {
        List<User> userList = userRepository.findAll();
        return userList.stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserDTO updateUser(UserDTO userDTO) {
        // 1. 기존 사용자 조회
        User user = userRepository.findByUserId(userDTO.getUserId());
        if (user == null) {
            throw new IllegalArgumentException("해당 유저를 찾을 수 없습니다.");
        }

        user.setEmail(userDTO.getEmail());
        user.setHometown(userDTO.getHometown());
        user.setCountry(userDTO.getCountry());
        if (userDTO.getUserPw() != null && !userDTO.getUserPw().isEmpty()) {
            user.setUserPw(bCryptPasswordEncoder.encode(userDTO.getUserPw()));
        }
        user.setRole("ROLE_ADMIN");

        List<TastePreferenceDTO> newTasteDTOList = userDTO.getTastePreference();
        List<TastePreference> existingTasteList = user.getTastePreferences();
        if (existingTasteList == null) {
            existingTasteList = new java.util.ArrayList<>();
            user.setTastePreferences(existingTasteList);
        }

        int newSize = (newTasteDTOList != null) ? newTasteDTOList.size() : 0;
        int oldSize = existingTasteList.size();
        int commonSize = Math.min(newSize, oldSize);

        for (int i = 0; i < commonSize; i++) {
            TastePreference existingTP = existingTasteList.get(i);
            TastePreferenceDTO newTP = newTasteDTOList.get(i);
            existingTP.setTaste(newTP.getTaste());
            existingTP.setSpicyLevel(newTP.getSpicyLevel());
        }

        if (newSize > oldSize) {
            for (int i = oldSize; i < newSize; i++) {
                TastePreferenceDTO newTP = newTasteDTOList.get(i);
                TastePreference tp = new TastePreference();
                tp.setTaste(newTP.getTaste());
                tp.setSpicyLevel(newTP.getSpicyLevel());
                tp.setUser(user);
                existingTasteList.add(tp);
            }
        }
        else if (oldSize > newSize) {
            for (int i = oldSize - 1; i >= newSize; i--) {
                TastePreference removed = existingTasteList.remove(i);
            }
        }

        user = userRepository.saveAndFlush(user);

        return UserMapper.toDTO(user);
    }






    public boolean checkPassword(String userId, String rawPassword) {
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new IllegalArgumentException("해당 유저를 찾을 수 없습니다.");
        }
        return bCryptPasswordEncoder.matches(rawPassword, user.getUserPw());
    }

    public UserDetailDTO getUserDetail(String userId) {
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new IllegalArgumentException("해당 유저를 찾을 수 없습니다.");
        }

        List<RecipeSummaryDTO> myRecipes = recipeRepository.findByUser(user)
                .stream()
                .limit(4)
                .map(recipe -> RecipeSummaryDTO.builder()
                        .recipeId(recipe.getRecipeId())
                        .foodImage(recipe.getFoodImage())
                        .name(recipe.getName())
                        .build())
                .collect(Collectors.toList());

        List<RecipeSummaryDTO> savedRecipes = scrapRepository.findByUser(user)
                .stream()
                .limit(4)
                .map(scrap -> {
                    Recipe recipe = scrap.getRecipe();
                    return RecipeSummaryDTO.builder()
                            .recipeId(recipe.getRecipeId())
                            .foodImage(recipe.getFoodImage())
                            .name(recipe.getName())
                            .build();
                })
                .collect(Collectors.toList());

        return UserDetailDTO.builder()
                .userId(user.getUserId())
                .profilePic(user.getProfilePic())
                .hometown(user.getHometown())
                .country(user.getCountry())
                .role(user.getRole())
                .myRecipes(myRecipes)
                .savedRecipes(savedRecipes)
                .build();
    }

    public List<RecipeSummaryDTO> getAllMyRecipes(String userId) {
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new IllegalArgumentException("해당 유저를 찾을 수 없습니다.");
        }
        return recipeRepository.findByUser(user)
                .stream()
                .map(recipe -> RecipeSummaryDTO.builder()
                        .recipeId(recipe.getRecipeId())
                        .foodImage(recipe.getFoodImage())
                        .name(recipe.getName())
                        .build())
                .collect(Collectors.toList());
    }

    public List<RecipeSummaryDTO> getAllSavedRecipes(String userId) {
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new IllegalArgumentException("해당 유저를 찾을 수 없습니다.");
        }
        return scrapRepository.findByUser(user)
                .stream()
                .map(scrap -> {
                    Recipe recipe = scrap.getRecipe();
                    return RecipeSummaryDTO.builder()
                            .recipeId(recipe.getRecipeId())
                            .foodImage(recipe.getFoodImage())
                            .name(recipe.getName())
                            .build();
                })
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(String userId) {
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new IllegalArgumentException("해당 유저를 찾을 수 없습니다.");
        }
        return UserMapper.toDTO(user);
    }
}
