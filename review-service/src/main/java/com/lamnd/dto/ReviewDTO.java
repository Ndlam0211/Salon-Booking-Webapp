package com.lamnd.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ReviewDTO(
    Long id,
    UserDTO user,
    SalonDTO salon,
    String reviewContent,
    double rating,
    LocalDate createdAt
) {
}
