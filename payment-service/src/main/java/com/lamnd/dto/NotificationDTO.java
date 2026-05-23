package com.lamnd.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record NotificationDTO(
        Long id,
        String type,
        String description,
        Boolean isRead,
        Long userId,
        Long bookingId,
        Long salonId,
        BookingDTO booking,
        LocalDateTime createdAt
) {
}
