package com.garagehub.global;

import com.garagehub.global.common.ApiResponse;
import com.garagehub.global.exception.CustomException;
import com.garagehub.global.exception.ErrorCode;
import com.garagehub.global.exception.ErrorDetail;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<?>> handleCustomException(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();

        log.warn("비즈니스 예외: {}", errorCode.getCode());

        ErrorDetail detail = ErrorDetail.builder()
            .errorCode(errorCode)
            .data(e.getData())
            .build();

        return toResponse(detail);
    }

    @ExceptionHandler(RedisConnectionFailureException.class)
    public ResponseEntity<ApiResponse<?>> handleRedisConnection(RedisConnectionFailureException e) {
        log.error("Redis 연결 실패", e);

        ErrorDetail detail = ErrorDetail.builder()
            .errorCode(ErrorCode.REDIS_CONNECTION_FAILED)
            .build();

        return toResponse(detail);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidation(MethodArgumentNotValidException e) {
        ErrorCode errorCode = ErrorCode.INVALID_INPUT;

        log.warn("검증 예외: {}", errorCode.getCode());

        List<ApiResponse.FieldError> errors = e.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(fieldError -> new ApiResponse.FieldError(
                fieldError.getField(),
                errorCode.getCode(),
                fieldError.getDefaultMessage()
            ))
            .toList();

        ErrorDetail detail = ErrorDetail.builder()
            .errorCode(errorCode)
            .errors(errors)
            .build();

        return toResponse(detail);
    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleDataIntegrity(DataIntegrityViolationException e) {
        log.error("데이터 무결성 위반", e);

        ErrorDetail detail = ErrorDetail.builder()
            .errorCode(ErrorCode.DATA_INTEGRITY_VIOLATION)
            .build();

        return toResponse(detail);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleConstraintViolation(ConstraintViolationException e) {
        ErrorCode errorCode = ErrorCode.INVALID_INPUT;

        log.warn("파라미터 검증 예외: {}", errorCode.getCode());

        List<ApiResponse.FieldError> errors = e.getConstraintViolations()
            .stream()
            .map(violation -> {
                String path = violation.getPropertyPath().toString();
                String field = path.substring(path.lastIndexOf('.') + 1);
                return new ApiResponse.FieldError(field, errorCode.getCode(), violation.getMessage());
            })
            .toList();

        ErrorDetail detail = ErrorDetail.builder()
            .errorCode(errorCode)
            .errors(errors)
            .build();

        return toResponse(detail);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception e) {
        log.error("예상 못한 예외 발생", e);

        ErrorDetail detail = ErrorDetail.builder()
            .errorCode(ErrorCode.UNEXPECTED_ERROR)
            .build();

        return toResponse(detail);
    }

    // --- 공통 변환 ---
    private ResponseEntity<ApiResponse<?>> toResponse(ErrorDetail detail) {
        ErrorCode errorCode = detail.getErrorCode();

        return ResponseEntity
            .status(errorCode.getStatus())
            .body(ApiResponse.builder()
                .message(errorCode.getMessage())
                .data(detail.getData())
                .errors(detail.getErrors())
                .fail());
    }
}