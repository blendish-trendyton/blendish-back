package com.example.blendish.controller;

import com.example.blendish.domain.recipe.dto.RecipeSummaryDTO;
import com.example.blendish.domain.user.dto.UserDTO;
import com.example.blendish.domain.user.dto.check.CheckUserPwDTO;
import com.example.blendish.domain.user.dto.preference.UserDetailDTO;
import com.example.blendish.domain.user.service.UserService;
import com.example.blendish.global.dto.ApiResponseTemplate;
import com.example.blendish.global.response.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "User Controller", description = "사용자 관리 관련 API")
@Controller
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "전체 사용자 조회",
            description = "저장된 전체 사용자 데이터를 반환한다."
    )
    @GetMapping("/all")
    public ResponseEntity<ApiResponseTemplate<List<UserDTO>>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponseTemplate.success(SuccessCode.OK, users));
    }

    @Operation(
            summary = "사용자 정보 업데이트",
            description = "전체 사용자 정보를 전달받아 유저 정보를 업데이트 한다."
    )
    @PutMapping(value = "/update", consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResponseTemplate<UserDTO>> updateUser(
            @RequestPart("user") UserDTO userDTO,
            @RequestPart(value = "profilePic", required = false) MultipartFile profilePic
    ) throws IOException {
        UserDTO updatedUser = userService.updateUser(userDTO, profilePic);
        return ResponseEntity.ok(ApiResponseTemplate.success(SuccessCode.OK, updatedUser));
    }

    @Operation(
            summary = "비밀번호 확인",
            description = "입력받은 비밀번호가 저장된 비밀번호와 동일한지 확인한다."
    )
    @PostMapping("/check")
    public ResponseEntity<ApiResponseTemplate<Boolean>> checkPassword(@RequestBody CheckUserPwDTO dto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        System.out.println(userId);

        boolean result = userService.checkPassword(userId, dto.getPassword());
        return ResponseEntity.ok(ApiResponseTemplate.success(SuccessCode.OK, result));
    }

    @Operation(
            summary = "현재 로그인된 사용자 조회",
            description = "토큰에 포함된 사용자 ID를 기반으로 전체 사용자 데이터를 반환한다."
    )
    @GetMapping("/me")
    public ResponseEntity<ApiResponseTemplate<UserDetailDTO>> getUserById() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        System.out.println(userId);
        UserDetailDTO userDetail = userService.getUserDetail(userId);
        return ResponseEntity.ok(ApiResponseTemplate.success(SuccessCode.OK, userDetail));
    }


    @Operation(
            summary = "내 레시피 전체 조회",
            description = "현재 로그인한 사용자가 직접 등록한 전체 레시피 목록을 조회하여 반환한다."
    )
    @GetMapping("/myrecipes")
    public ResponseEntity<ApiResponseTemplate<List<RecipeSummaryDTO>>> getAllMyRecipes() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        List<RecipeSummaryDTO> myRecipes = userService.getAllMyRecipes(userId);
        return ResponseEntity.ok(ApiResponseTemplate.success(SuccessCode.OK, myRecipes));
    }

    @Operation(
            summary = "저장한 레시피 전체 조회",
            description = "현재 로그인한 사용자가 스크랩한 전체 레시피 목록(요약 정보)을 반환한다."
    )
    @GetMapping("/savedrecipes")
    public ResponseEntity<ApiResponseTemplate<List<RecipeSummaryDTO>>> getAllSavedRecipes() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        List<RecipeSummaryDTO> savedRecipes = userService.getAllSavedRecipes(userId);
        return ResponseEntity.ok(ApiResponseTemplate.success(SuccessCode.OK, savedRecipes));
    }
    @Operation(
            summary = "현재 로그인한 사용자 기본 정보 조회",
            description = "현재 로그인한 사용자의 기본 정보를 반환한다. (레시피 정보 제외)"
    )
    @GetMapping("/me/details")
    public ResponseEntity<ApiResponseTemplate<UserDTO>> getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        UserDTO userDTO = userService.getUserById(userId);
        return ResponseEntity.ok(ApiResponseTemplate.success(SuccessCode.OK, userDTO));
    }
}
