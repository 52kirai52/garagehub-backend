package com.garagehub.global.common;
import java.util.List;
import lombok.Builder;
@Builder
public record ApiResponse<T>(
    boolean success,
    String message,
    T data,
    List<FieldError> errors
) {
        public ApiResponse {
        if (errors == null) {
            errors = List.of();
        }
    }

    public static class ApiResponseBuilder<T> {
        public ApiResponse<T> ok() {
            this.success = true;
            return this.build();
        }
        public ApiResponse<T> fail() {
            this.success = false;
            return this.build();
        }
    }

    public record FieldError(
        String field,
        String code
    ) {}
}