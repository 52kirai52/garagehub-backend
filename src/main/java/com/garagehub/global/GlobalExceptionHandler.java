package com.garagehub.global;

import com.garagehub.global.common.ApiResponse;
import com.garagehub.global.exception.CustomException;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
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
        List<ApiResponse.FieldError> errors = List.of(
            new ApiResponse.FieldError("server", "S000", "서버 에러가 발생했습니다.")
        );
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.fail(errors));
    }
}