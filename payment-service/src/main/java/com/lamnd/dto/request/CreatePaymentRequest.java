package com.lamnd.dto.request;

import com.lamnd.dto.UserDTO;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePaymentRequest {

    private UserDTO user;

    private Double amount;

    private Long orderId;

    private String description;

    private String returnUrl;

    private String cancelUrl;
}
