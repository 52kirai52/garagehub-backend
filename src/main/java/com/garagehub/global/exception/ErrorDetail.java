package com.garagehub.global.exception;

import com.garagehub.global.common.ApiResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ErrorDetail {
    private final ErrorCode errorCode;                    // status, code, message 출처
    private final String message;                         // 대표 message (수동 지정용, 없으면 null)
    private final List<ApiResponse.FieldError> errors;    // 없을 수 있음 (null)
    private final Object data;                            // 없을 수 있음 (null)
}