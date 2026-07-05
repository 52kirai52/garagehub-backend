package com.garagehub.global.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;
    private final Object data;

    public CustomException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.data = null;
    }

    public CustomException(ErrorCode errorCode, Object data) {
        this.errorCode = errorCode;
        this.data = data;
    }

    public HttpStatus getStatus() {
        return errorCode.getStatus();
    }

    public String getCode() {
        return errorCode.getCode();
    }

    @Override
    public String getMessage() {
        return errorCode.getMessage();
    }
}
