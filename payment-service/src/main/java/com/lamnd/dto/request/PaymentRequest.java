package com.lamnd.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Builder
public class PaymentRequest {

    private String transactionId;

    private String sessionId;

    private String paymentLinkId;

    private String callbackData;

    private Map<String, Object> metadata;
}
