package com.garagehub.global.common;

public record ApiResponse<T>(
    boolean success,
    String message,
    T data
) {
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, "성공", data);
    }

    public static <T> ApiResponse<T> ok() {
        return new ApiResponse<>(true, "성공", null);
    }

    public static <T> ApiResponse<T> fail(String message) {
        return new ApiResponse<>(false, message, null);
    }
}