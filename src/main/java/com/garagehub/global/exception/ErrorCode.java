package com.garagehub.global.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

// 인증 (AUTH)
    DUPLICATE_PHONE(HttpStatus.BAD_REQUEST, "AUTH_001", "이미 가입된 전화번호입니다."),
    SMS_LOCK(HttpStatus.BAD_REQUEST, "AUTH_002", "요청을 처리 중입니다. 잠시 후 다시 시도해주세요."),
    SMS_COOLDOWN(HttpStatus.BAD_REQUEST, "AUTH_003", "잠시 후 다시 시도해주세요."),
    SMS_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH_004", "인증번호 발송에 실패했습니다."),
    SMS_INVALID_CODE(HttpStatus.BAD_REQUEST, "AUTH_005", "인증번호가 올바르지 않습니다."),

    // 공통 (COMMON)
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "COMMON_001", "올바른 입력값이 아닙니다."),
    REDIS_CONNECTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_002", "일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요."),
    UNEXPECTED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_000", "서버 에러가 발생했습니다.");
    
    private final HttpStatus status;
    private final String code;
    private final String message;

}