package com.example.blendish.domain.user.controller;

import com.example.blendish.domain.user.dto.CheckUserIdDTO;
import com.example.blendish.domain.user.dto.JoinDTO;
import com.example.blendish.domain.user.dto.UserDTO;
import com.example.blendish.domain.user.dto.preference.UserPreferencesDTO;
import com.example.blendish.domain.user.entity.User;
import com.example.blendish.domain.user.service.JoinService;
import com.example.blendish.global.dto.ApiResponseTemplate;
import com.example.blendish.global.response.ErrorCode;
import com.example.blendish.global.response.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "JoinController",description = "로그인, 회원가입 관련 API")
@Controller
@RequestMapping("/join")
public class JoinController {

    private final JoinService joinService;

    public JoinController(JoinService joinService) {
        this.joinService = joinService;
    }

    @Operation(
            summary = "User ID 중복 체크",
            description = "입력한 UserId가 이미 존재하는지 확인합니다."
    )
    @PostMapping("/check")
    public ResponseEntity<ApiResponseTemplate<Boolean>> checkUserId(@RequestBody CheckUserIdDTO dto) {
        boolean exists = joinService.checkUserId(dto.getUserId());
        return ResponseEntity.ok(ApiResponseTemplate.success(SuccessCode.OK, exists));
    }
    @Operation(
            summary = "회원가입",
            description = "회원가입 진행. ID,PW,이메일을 받으며 해당 정보들을 반환해준다."
    )
    @PostMapping("/user")
    public ResponseEntity<ApiResponseTemplate<?>> createUser(@RequestBody JoinDTO joinDTO) {
        try {
            User savedUser = joinService.joinProcess(joinDTO);
            return ResponseEntity.ok(ApiResponseTemplate.success(SuccessCode.CREATED, savedUser));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseTemplate.error(ErrorCode.BAD_REQUEST));
        }

    }
    @Operation(
            summary = "사용자 맛 선호도 업데이트",
            description = "사용자의 선호도(취향)를 업데이트하고, 업데이트된 사용자 정보를 반환한다."
    )
    @PostMapping("/preferences")
    public ResponseEntity<ApiResponseTemplate<UserDTO>> updateUserPreferences(@RequestBody UserPreferencesDTO dto) {
        try {
            UserDTO updatedUser = joinService.updateUserPreferences(dto);
            return ResponseEntity.ok(ApiResponseTemplate.success(SuccessCode.CREATED, updatedUser));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponseTemplate.error(ErrorCode.NOT_FOUND));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseTemplate.error(ErrorCode.INTERNAL_SERVER_ERROR));
        }
    }

}
