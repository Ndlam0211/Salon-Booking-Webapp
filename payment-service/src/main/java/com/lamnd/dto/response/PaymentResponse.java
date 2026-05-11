package com.lamnd.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lamnd.enums.PaymentStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PaymentResponse(
        Long id,
        Long amount,
        PaymentStatus status,
        String paymentLinkId,
        Long userId,
        Long bookingId,
        Long salonId
) {
}
