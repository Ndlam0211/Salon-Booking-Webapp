package com.lamnd.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lamnd.enums.BookingStatus;

import java.time.LocalDateTime;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BookingResponse(
        Long id,
        Long salonId,
        Long customerId,
        Set<Long> serviceIds,
        LocalDateTime startTime,
        LocalDateTime endTime,
        BookingStatus status,
        Double totalPrice
) {
}
