package com.lamnd.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {

    private String transactionId;

    private String sessionId;

    private String paymentLinkId;

    private String callbackData;

    private Map<String, Object> metadata;
}
