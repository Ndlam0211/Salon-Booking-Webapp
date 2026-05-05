package com.lamnd.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record SalonDTO(
    Long id,
    String name,
    List<String> images,
    String address,
    String phoneNumber,
    String email,
    String city,
    Long ownerId,
    String openingTime,
    String closingTime
) {
}
