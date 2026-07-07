package com.garagehub.domain.auth.controller;

import com.garagehub.domain.auth.dto.SendCodeRequest;
import com.garagehub.domain.auth.dto.SendVerificationResponse;
import com.garagehub.domain.auth.dto.VerifyCodeRequest;
import com.garagehub.domain.auth.service.AuthService;
import com.garagehub.global.common.ApiResponse;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/send-code")
    public ResponseEntity<ApiResponse<SendVerificationResponse>> sendCode(@RequestBody @Valid SendCodeRequest request) {
        long expiresIn = authService.sendVerificationCode(request.getPhone());

        return ResponseEntity.ok(
            ApiResponse.<SendVerificationResponse>builder()
                .message("인증번호가 발송되었습니다.")
                .data(SendVerificationResponse.builder()
                    .expiresIn(expiresIn)
                    .build())
                .ok()
        );
    }

    @PostMapping("/verify-code")
    public ResponseEntity<String> verifyCode(@RequestBody @Valid VerifyCodeRequest request) {
        boolean result = authService.verifyCode(request.getPhone(), request.getCode());
        if (result) {
            return ResponseEntity.ok("인증이 완료되었습니다.");
        }
        return ResponseEntity.badRequest().body("인증번호가 올바르지 않습니다.");
    }
}