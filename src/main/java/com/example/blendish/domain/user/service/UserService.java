package com.example.blendish.domain.user.service;

import com.example.blendish.domain.recipe.dto.RecipeSummaryDTO;
import com.example.blendish.domain.recipe.entity.Recipe;
import com.example.blendish.domain.recipe.repository.RecipeRepository;
import com.example.blendish.domain.recipe.repository.ScrapRepository;
import com.example.blendish.domain.user.config.UserMapper;
import com.example.blendish.domain.user.dto.UserDTO;
import com.example.blendish.domain.user.dto.preference.UserDetailDTO;
import com.example.blendish.domain.user.entity.User;
import com.example.blendish.domain.user.repository.UserRepository;
import com.example.blendish.global.dto.ApiResponseTemplate;
import com.example.blendish.global.s3.S3UploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final ScrapRepository scrapRepository;
    private final S3UploadService s3UploadService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, RecipeRepository recipeRepository, ScrapRepository scrapRepository, S3UploadService s3UploadService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
        this.scrapRepository = scrapRepository;
        this.s3UploadService = s3UploadService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public List<UserDTO> getAllUsers() {
        List<User> userList = userRepository.findAll();
        return userList.stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    public UserDTO updateUser(UserDTO userDTO, MultipartFile profilePic) throws IOException {

        if (profilePic != null && !profilePic.isEmpty()) {
            String uploadedUrl = s3UploadService.saveFile(profilePic);
            userDTO.setProfilePic(uploadedUrl);
        }

        userDTO.setRole("ROLE_ADMIN");

        if (userDTO.getUserPw() != null && !userDTO.getUserPw().isEmpty()) {
            userDTO.setUserPw(bCryptPasswordEncoder.encode(userDTO.getUserPw()));
        }

        User existingUser = userRepository.findByUserId(userDTO.getUserId());
        if (existingUser != null) {
            userRepository.delete(existingUser);
            userRepository.flush();
        }

        User newUser = UserMapper.toEntity(userDTO);  // 기존 UserMapper를 그대로 사용
        newUser = userRepository.save(newUser);
        return UserMapper.toDTO(newUser);
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
