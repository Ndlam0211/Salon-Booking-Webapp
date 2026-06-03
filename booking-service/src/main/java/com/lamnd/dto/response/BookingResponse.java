package com.lamnd.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lamnd.dto.SalonDTO;
import com.lamnd.dto.ServiceDTO;
import com.lamnd.dto.UserDTO;
import com.lamnd.enums.BookingStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record BookingResponse(
        Long id,
        Long salonId,
        Long customerId,
        Set<Long> serviceIds,
        LocalDateTime startTime,
        LocalDateTime endTime,
        BookingStatus status,
        Double totalPrice,
        Set<ServiceDTO> services,
        SalonDTO salon,
        UserDTO customer
) {
}
