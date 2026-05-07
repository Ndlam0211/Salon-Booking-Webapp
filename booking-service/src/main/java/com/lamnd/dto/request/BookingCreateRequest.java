package com.lamnd.dto.request;

import java.time.LocalDateTime;
import java.util.Set;

public record BookingCreateRequest(
        Set<Long> serviceIds,
        LocalDateTime endTime,
        LocalDateTime startTime
) {
}
