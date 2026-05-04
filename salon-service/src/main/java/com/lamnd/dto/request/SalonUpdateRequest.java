package com.lamnd.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record SalonUpdateRequest(
    @NotBlank(message = "Name is required")
    String name,
    List<String> images,
    String address,
    String phoneNumber,
    @Email
    String email,
    String city,
    String openingTime,
    String closingTime
) {
}
