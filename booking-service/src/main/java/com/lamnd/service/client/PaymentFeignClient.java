package com.lamnd.service.client;

import com.lamnd.common.ApiResponse;
import com.lamnd.dto.response.BookingResponse;
import com.lamnd.enums.PaymentMethod;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("PAYMENT-SERVICE")
public interface PaymentFeignClient {

    @PostMapping("/api/v1/payments")
    ApiResponse<?> createPaymentLink(
            @RequestBody BookingResponse bookingDTO,
            @RequestParam PaymentMethod paymentMethod,
            @RequestHeader("Authorization") String token
    );
}
