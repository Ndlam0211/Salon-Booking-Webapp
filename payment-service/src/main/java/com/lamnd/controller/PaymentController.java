package com.lamnd.controller;

import com.lamnd.common.ApiResponse;
import com.lamnd.common.BaseController;
import com.lamnd.dto.BookingDTO;
import com.lamnd.dto.UserDTO;
import com.lamnd.dto.request.PaymentRequest;
import com.lamnd.dto.response.PaymentLinkResponse;
import com.lamnd.entity.Payment;
import com.lamnd.enums.PaymentMethod;
import com.lamnd.service.PaymentService;
import com.lamnd.service.client.UserFeignClient;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.ObjectMapper;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/payments")
@Slf4j
public class PaymentController extends BaseController {

    private final PaymentService paymentService;
    private final UserFeignClient userFeignClient;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> createPaymentLink(
            @RequestBody BookingDTO bookingDTO,
            @RequestParam PaymentMethod paymentMethod,
            @RequestHeader("Authorization") String token
    ) throws StripeException {

        ObjectMapper objectMapper = new ObjectMapper();

        UserDTO user = objectMapper.convertValue(
                Objects.requireNonNull(userFeignClient.getMyInfo(token).getBody()).data(),
                UserDTO.class
        );

        PaymentLinkResponse response = paymentService.createOrder(user, bookingDTO, paymentMethod);

        return createSuccessResponse(response);
    }

    @GetMapping("/{paymentId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> getPaymentById(
            @PathVariable Long paymentId
    )  {
        Payment response = paymentService.getPaymentById(paymentId);

        return createSuccessResponse(response);
    }

    @PatchMapping("/{paymentId}/proceed")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> proceedPayment(
            @PathVariable("paymentId") Long paymentId,
            @RequestBody PaymentRequest paymentRequest
    )  {
        Payment payment = paymentService.getPaymentById(paymentId);

        Boolean response = paymentService.proceedPayment(payment, paymentRequest);

        return createSuccessResponse(response);
    }
}
