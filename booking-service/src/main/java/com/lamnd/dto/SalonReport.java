package com.lamnd.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record SalonReport(
        Long salonId,
        String salonName,
        Integer totalBookings,
        Double totalEarnings,
        Integer cancelledBookings,
        Double totalRefund
) {
}
