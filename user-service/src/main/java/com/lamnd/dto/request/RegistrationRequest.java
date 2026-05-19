package com.lamnd.dto.request;

import com.lamnd.annotation.UniqueEmail;
import com.lamnd.annotation.UniqueUsername;
import com.lamnd.annotation.ValidEmail;
import com.lamnd.enums.UserRole;
import jakarta.validation.constraints.NotBlank;

public record RegistrationRequest(
        @NotBlank(message = "Username is required")
        @UniqueUsername
        String username,
        String password,
        @NotBlank(message = "Email is required")
        @ValidEmail
        @UniqueEmail
        String email,
        String firstName,
        String lastName,
        UserRole role
) {
}
