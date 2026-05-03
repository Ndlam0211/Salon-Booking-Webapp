package com.lamnd.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public record UserResponse(
    Long id,
    String username,
    String email,
    String fullName,
    String phoneNumber,
    String role
)  {}
