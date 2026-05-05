package com.lamnd.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CategoryUpdateRequest(
    @NotBlank(message = "Category name must not be blank")
    String name,
    String image
) {
}
