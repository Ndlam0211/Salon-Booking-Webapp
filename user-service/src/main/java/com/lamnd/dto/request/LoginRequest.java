package com.lamnd.dto.request;

public record LoginRequest(
        String username,
        String password
) {
}
