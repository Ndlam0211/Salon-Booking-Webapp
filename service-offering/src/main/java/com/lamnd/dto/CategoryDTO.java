package com.lamnd.dto;

import lombok.Builder;

@Builder
public record CategoryDTO(
    Long id,
    String name,
    String image
) {
}
