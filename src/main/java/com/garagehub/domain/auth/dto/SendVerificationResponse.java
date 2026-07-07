package com.garagehub.domain.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SendVerificationResponse {
    private final long expiresIn;
}
