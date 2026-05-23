package com.lamnd.dto.request;

public record ReviewRequest(
        String reviewContent,
        double rating
) {
}
