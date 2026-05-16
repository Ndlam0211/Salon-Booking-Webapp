package com.lamnd.dto.request;

public record RegistrationRequest(
    String username,
    String password,
    String email,
    String firstName,
    String lastName
) {
}
