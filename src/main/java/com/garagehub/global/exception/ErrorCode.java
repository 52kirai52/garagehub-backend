package com.garagehub.global.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 비즈니스 예외
    DUPLICATE_PHONE(HttpStatus.BAD_REQUEST, "AUTH_001", "이미 가입된 전화번호입니다."),
    SMS_LOCK(HttpStatus.BAD_REQUEST, "AUTH_002", "요청을 처리 중입니다. 잠시 후 다시 시도해주세요."),
    SMS_COOLDOWN(HttpStatus.BAD_REQUEST, "AUTH_003", "발송 횟수를 초과했습니다. 잠시 후 다시 시도해주세요"),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "COMMON_001", "올바른 입력값이 아닙니다."),
    SMS_INVALID_CODE(HttpStatus.BAD_REQUEST, "AUTH_005", "인증번호가 올바르지 않습니다."),
    PHONE_NOT_VERIFIED(HttpStatus.BAD_REQUEST, "AUTH_006", "전화번호 인증이 필요합니다."),
    DUPLICATE_USERNAME(HttpStatus.BAD_REQUEST, "AUTH_007", "이미 사용중인 아이디입니다."),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "AUTH_008", "이미 사용중인 이메일입니다."),

    // 시스템 예외
    SMS_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH_004", "인증번호 발송에 실패했습니다."),
    REDIS_CONNECTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_002", "일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요."),
    DATA_INTEGRITY_VIOLATION(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_003", "요청을 처리할 수 없습니다."),
    UNEXPECTED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_000", "서버 에러가 발생했습니다.");
    
    private final HttpStatus status;
    private final String code;
    private final String message;

}