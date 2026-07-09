package com.garagehub.domain.user.controller;

import com.garagehub.domain.user.dto.SignUpRequest;
import com.garagehub.domain.user.service.UserService;
import com.garagehub.global.common.ApiResponse;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Boolean> checkUsername(@RequestParam String username) {
        return ResponseEntity.ok(userService.checkUsername(username));
    }
}