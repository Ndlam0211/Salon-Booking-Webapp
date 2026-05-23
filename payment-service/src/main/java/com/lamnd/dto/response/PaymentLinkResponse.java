package com.lamnd.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentLinkResponse {
    private String paymentLinkUrl;
    private String paymentLinkId;
}
