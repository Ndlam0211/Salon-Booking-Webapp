package com.lamnd.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BookingSlotResponse(
        LocalDateTime startTime,
        LocalDateTime endTime
) {
}
