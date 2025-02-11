package com.example.blendish.controller;

import com.example.blendish.domain.user.dto.UserDTO;
import com.example.blendish.domain.user.service.UserService;
import com.example.blendish.global.dto.ApiResponseTemplate;
import com.example.blendish.global.response.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
