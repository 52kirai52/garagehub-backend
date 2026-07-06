package com.garagehub.global.exception;

import com.garagehub.global.common.ApiResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ErrorDetail {
    private final ErrorCode errorCode;
    private final List<ApiResponse.FieldError> errors;
    private final Object data;
}