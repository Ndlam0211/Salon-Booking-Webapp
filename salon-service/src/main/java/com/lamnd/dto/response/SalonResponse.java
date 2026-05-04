package com.lamnd.dto.response;

import java.util.List;

public record SalonResponse(
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
