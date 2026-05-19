package com.lamnd.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ServiceOfferingCreateRequest(
    @NotBlank(message = "Service offering name must not be blank")
    String name,
    String description,
    Double price,
    int duration,
    String image,
    Long categoryId
) {
}
