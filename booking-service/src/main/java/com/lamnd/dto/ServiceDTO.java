package com.lamnd.dto;

import lombok.Builder;

@Builder
public record ServiceDTO(
    Long id,
    String name,
    String description,
    Double price,
    int duration,
    String image,
    Long salonId,
    Long categoryId
) {
}
