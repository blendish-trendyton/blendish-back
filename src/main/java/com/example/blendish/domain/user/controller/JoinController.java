package com.example.blendish.domain.user.controller;

import com.example.blendish.domain.user.dto.CheckUserIdDTO;
import com.example.blendish.domain.user.dto.JoinDTO;
import com.example.blendish.domain.user.dto.UserDTO;
import com.example.blendish.domain.user.dto.UserPreferencesDTO;
import com.example.blendish.domain.user.entity.User;
import com.example.blendish.domain.user.service.JoinService;
import com.example.blendish.global.dto.ApiResponseTemplate;
import com.example.blendish.global.response.ErrorCode;
import com.example.blendish.global.response.SuccessCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;

@Controller
@RequestMapping("/join")
public class JoinController {

    private final JoinService joinService;

    public JoinController(JoinService joinService) {
        this.joinService = joinService;
    }

    @PostMapping("/check")
    public ResponseEntity<ApiResponseTemplate<Boolean>> checkUserId(@RequestBody CheckUserIdDTO dto) {
        boolean exists = joinService.checkUserId(dto.getUserId());
        return ResponseEntity.ok(ApiResponseTemplate.success(SuccessCode.OK, exists));
    }

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
