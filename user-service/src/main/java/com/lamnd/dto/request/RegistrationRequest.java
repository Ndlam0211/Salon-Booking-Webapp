package com.lamnd.dto.request;

import com.lamnd.enums.UserRole;

public record RegistrationRequest(
    String username,
    String password,
    String email,
    String firstName,
    String lastName,
    UserRole role
) {
}
