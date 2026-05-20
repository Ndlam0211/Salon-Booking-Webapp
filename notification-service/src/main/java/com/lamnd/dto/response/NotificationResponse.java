package com.lamnd.dto.response;

import com.lamnd.dto.BookingDTO;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record NotificationResponse(
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
