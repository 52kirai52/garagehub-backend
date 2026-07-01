package com.garagehub.global.common;

import java.util.List;

public record ApiResponse<T>(
    boolean success,
    T data,
    List<FieldError> errors
) {
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, data, List.of());
    }

    public static <T> ApiResponse<T> ok() {
        return new ApiResponse<>(true, null, List.of());
    }

    public static ApiResponse<?> fail(List<FieldError> errors) {
        return new ApiResponse<>(false, null, errors);
    }
    
    public static <T> ApiResponse<T> fail(T data, List<FieldError> errors) {
        return new ApiResponse<>(false, data, errors);
    }

    public record FieldError(
        String field,
        String code,
        String message
    ) {}
}