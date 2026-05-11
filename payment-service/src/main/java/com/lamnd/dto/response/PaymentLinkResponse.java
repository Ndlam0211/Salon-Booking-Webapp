package com.lamnd.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PaymentLinkResponse(
        String paymentLinkUrl,
        String getPaymentLinkId
) {
}
