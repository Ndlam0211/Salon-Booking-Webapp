package com.lamnd.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lamnd.enums.PaymentMethod;
import com.lamnd.enums.PaymentStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PaymentResponse(
        Long id,
        Long amount,
        PaymentStatus status,
        PaymentMethod paymentMethod,
        String paymentLinkId,
        Long userId,
        Long bookingId,
        Long salonId
) {
}
