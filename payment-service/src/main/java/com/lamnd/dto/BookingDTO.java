package com.lamnd.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BookingDTO(
        Long id,
        Long salonId,
        Long customerId,
        Set<Long> serviceIds,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Double totalPrice
) {
}
