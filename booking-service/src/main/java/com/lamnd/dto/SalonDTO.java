package com.lamnd.dto;

import lombok.Builder;

import java.time.LocalTime;
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
    LocalTime openingTime,
    LocalTime closingTime
) {
}
