package com.garagehub.global;

import com.garagehub.global.common.ApiResponse;
import com.garagehub.global.exception.CustomException;
import com.garagehub.global.exception.ErrorCode;

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

        return toResponse(errorCode, e.getData(), null);
    }

    @ExceptionHandler(RedisConnectionFailureException.class)
    public ResponseEntity<ApiResponse<?>> handleRedisConnection(RedisConnectionFailureException e) {
        log.error("Redis 연결 실패", e);

        return toResponse(ErrorCode.REDIS_CONNECTION_FAILED, null, null);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleDataIntegrity(DataIntegrityViolationException e) {
        log.error("데이터 무결성 위반", e);

        return toResponse(ErrorCode.DATA_INTEGRITY_VIOLATION, null, null);
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
                fieldError.getDefaultMessage()
            ))
            .toList();

        return toResponse(errorCode, null, errors);
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
                return new ApiResponse.FieldError(field, violation.getMessage());
            })
            .toList();

        return toResponse(errorCode, null, errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception e) {
        log.error("예상 못한 예외 발생", e);

        return toResponse(ErrorCode.UNEXPECTED_ERROR, null, null);
    }

    private ResponseEntity<ApiResponse<?>> toResponse(
            ErrorCode errorCode, Object data, List<ApiResponse.FieldError> fieldErrors) {

        return ResponseEntity
            .status(errorCode.getStatus())
            .body(ApiResponse.builder()
                .errorcode(errorCode.getCode())
                .message(errorCode.getMessage())
                .data(data)
                .fieldErrors(fieldErrors)
                .fail());
    }
}