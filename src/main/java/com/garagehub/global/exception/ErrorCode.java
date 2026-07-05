package com.garagehub.global.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 중복
    DUPLICATE_PHONE(HttpStatus.BAD_REQUEST, "D001", "이미 가입된 전화번호입니다."),

    // 인증
    SMS_LOCK(HttpStatus.BAD_REQUEST, "A001", "요청을 처리 중입니다. 잠시 후 다시 시도해주세요."),
    SMS_COOLDOWN(HttpStatus.BAD_REQUEST, "A002", "잠시 후 다시 시도해주세요."),

    // SMS
    SMS_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S001", "인증번호 발송에 실패했습니다."),

    // 인프라 (Redis 등)
    REDIS_CONNECTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "R001", "일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요."),

    // 예상치 못한 예외
    UNEXPECTED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S000", "서버 에러가 발생했습니다."),

    // 검증
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "V001", "올바른 입력값이 아닙니다.");
    
    private final HttpStatus status;
    private final String code;
    private final String message;

}