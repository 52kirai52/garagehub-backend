package com.garagehub.global.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
    boolean success,
    String message,
    T data,
    List<FieldError> errors
) {
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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record FieldError(
        String field,
        String code,
        String message
    ) {}
}