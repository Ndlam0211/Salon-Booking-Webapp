package com.lamnd.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CategoryCreateRequest(
    @NotBlank(message = "Category name must not be blank")
    String name,
    String image
) {
}
