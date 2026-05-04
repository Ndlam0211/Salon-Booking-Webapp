package com.lamnd.dto.request;

import java.util.List;

public record SalonUpdateRequest(
    String name,
    List<String> images,
    String address,
    String phoneNumber,
    String email,
    String city,
    String openingTime,
    String closingTime
) {
}
