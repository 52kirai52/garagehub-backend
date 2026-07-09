package com.garagehub.domain.user.controller;

import com.garagehub.domain.user.dto.SignUpRequest;
import com.garagehub.domain.user.service.UserService;
import com.garagehub.global.common.ApiResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signUp(@RequestBody @Valid SignUpRequest request) {
        userService.signUp(request);
        return ResponseEntity.ok(
            ApiResponse.<Void>builder()
                .message("회원가입이 완료되었습니다.")
                .ok()
        );
    }

    @GetMapping("/check-username")
    public ResponseEntity<ApiResponse<Void>> checkUsername(
            @RequestParam @NotBlank @Pattern(
                regexp = "^[a-z][a-z0-9]{3,19}$",
                message = "아이디 형식이 올바르지 않습니다."
            ) String username) {

        userService.validateUsername(username);

        return ResponseEntity.ok(
            ApiResponse.<Void>builder()
                .message("사용 가능한 아이디입니다.")
                .ok()
        );
    }

    @GetMapping("/check-email")
    public ResponseEntity<ApiResponse<Void>> checkEmail(
            @RequestParam @NotBlank @Email(message = "이메일 형식이 올바르지 않습니다.") String email) {

        userService.validateEmail(email);

        return ResponseEntity.ok(
            ApiResponse.<Void>builder()
                .message("사용 가능한 이메일입니다.")
                .ok()
        );
    }
}