package com.lamnd.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lamnd.enums.UserRole;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AuthResponse(
    String accessToken,
    String refreshToken,
    String message,
    String title,
    UserRole role
) {
}
