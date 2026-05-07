package com.lamnd.dto;

public record SalonReport(
        Long salonId,
        String salonName,
        Integer totalBookings,
        Double totalEarnings,
        Integer cancelledBookings,
        Double totalRefund
) {
}
