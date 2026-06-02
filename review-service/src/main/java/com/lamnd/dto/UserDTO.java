package com.lamnd.dto;

import lombok.Builder;

@Builder
public record UserDTO(
    Long id,
    String fullName,
    String email,
    String username
) {
}
