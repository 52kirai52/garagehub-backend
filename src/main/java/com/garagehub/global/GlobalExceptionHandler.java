package com.garagehub.global;

import com.garagehub.global.common.ApiResponse;
import com.garagehub.global.exception.CustomException;
import com.garagehub.global.exception.ErrorCode;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // 예상한 예외
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<?>> handleCustomException(CustomException e) {
        List<ApiResponse.FieldError> errors = List.of(
            new ApiResponse.FieldError(
                e.getField(),
                e.getCode(),
                e.getMessage()
            )
        );

        if (e.getData() != null) {
            return ResponseEntity
                .status(e.getStatus())
                .body(ApiResponse.fail(e.getData(), errors));
        }

        return ResponseEntity
            .status(e.getStatus())
            .body(ApiResponse.fail(errors));
    }

    // 예상 못한 예외
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception e) {
        log.error("예상 못한 예외 발생", e);

        ErrorCode errorCode = ErrorCode.UNEXPECTED_ERROR;
        List<ApiResponse.FieldError> errors = List.of(
            new ApiResponse.FieldError(
                errorCode.getField(),
                errorCode.getCode(),
                errorCode.getMessage()
            )
        );
        return ResponseEntity
            .status(errorCode.getStatus())
            .body(ApiResponse.fail(errors));
    }

    // 정체를 아는 예상 못한 예외
    
    // Redis(Memurai) 연결 실패
    @ExceptionHandler(RedisConnectionFailureException.class)
    public ResponseEntity<ApiResponse<?>> handleRedisConnection(RedisConnectionFailureException e) {
        log.error("Redis 연결 실패", e);

        ErrorCode errorCode = ErrorCode.REDIS_CONNECTION_FAILED;
        List<ApiResponse.FieldError> errors = List.of(
            new ApiResponse.FieldError(
                errorCode.getField(),
                errorCode.getCode(),
                errorCode.getMessage()
            )
        );
        return ResponseEntity
            .status(errorCode.getStatus())
            .body(ApiResponse.fail(errors));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiResponse<?>> handleValidation(MethodArgumentNotValidException e) {
            ErrorCode errorCode = ErrorCode.INVALID_INPUT;

            List<ApiResponse.FieldError> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new ApiResponse.FieldError(
                    fieldError.getField(),
                    errorCode.getCode(),
                    fieldError.getDefaultMessage()
                ))
                .toList();

            return ResponseEntity
                .status(errorCode.getStatus())
                .body(ApiResponse.fail(errors));
    }
}