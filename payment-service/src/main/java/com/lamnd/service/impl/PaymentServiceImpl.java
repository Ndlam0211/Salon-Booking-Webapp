package com.lamnd.service.impl;

import com.lamnd.dto.BookingDTO;
import com.lamnd.dto.UserDTO;
import com.lamnd.dto.request.CreatePaymentRequest;
import com.lamnd.dto.request.PaymentRequest;
import com.lamnd.dto.response.PaymentLinkResponse;
import com.lamnd.entity.Payment;
import com.lamnd.enums.PaymentMethod;
import com.lamnd.enums.PaymentStatus;
import com.lamnd.factory.PaymentProcessorFactory;
import com.lamnd.repository.PaymentRepo;
import com.lamnd.service.PaymentProcessor;
import com.lamnd.service.PaymentService;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepo paymentRepo;
    private final PaymentProcessorFactory paymentProcessorFactory;

    @Value("${payment.success-url}")
    private String successUrl;

    @Value("${payment.cancel-url}")
    private String cancelUrl;

    @Override
    @Transactional
    public PaymentLinkResponse createOrder(UserDTO user,
                                           BookingDTO booking,
                                           PaymentMethod paymentMethod) throws StripeException {
        // Validate inputs
        if (user == null || user.id() == null) {
            log.warn("Invalid user data for payment creation");
            throw new IllegalArgumentException("User information is required");
        }

        if (booking == null || booking.id() == null) {
            log.warn("Invalid booking data for payment creation");
            throw new IllegalArgumentException("Booking information is required");
        }

        if (paymentMethod == null) {
            log.warn("Payment method not specified");
            throw new IllegalArgumentException("Payment method must be specified");
        }

        Double amount = booking.totalPrice();
        if (amount == null || amount <= 0) {
            log.warn("Invalid amount for booking {}: {}", booking.id(), amount);
            throw new IllegalArgumentException("Invalid booking amount");
        }

        try {
            // Create payment record
            Payment newPayment = Payment.builder()
                    .amount(amount)
                    .paymentMethod(paymentMethod)
                    .userId(user.id())
                    .salonId(booking.salonId())
                    .bookingId(booking.id())
                    .status(PaymentStatus.PENDING)
                    .build();

            Payment savedPayment = paymentRepo.save(newPayment);
            log.info("Payment created with ID: {} for booking: {}", savedPayment.getId(), booking.id());

            // Get appropriate processor
            PaymentProcessor processor = paymentProcessorFactory.getProcessor(paymentMethod);

            // Create payment link
            String paymentUrl = processor.createPaymentLink(
                    CreatePaymentRequest.builder()
                            .amount(amount)
                            .orderId(savedPayment.getId())
                            .returnUrl(successUrl)
                            .cancelUrl(cancelUrl)
                            .description("Payment for salon appointment booking")
                            .build()
            );

            log.info("Payment link created successfully for payment ID: {}", savedPayment.getId());

            return PaymentLinkResponse.builder()
                    .paymentLinkUrl(paymentUrl)
                    .build();

        } catch (IllegalArgumentException e) {
            log.error("Validation error while creating payment: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error creating payment link for booking {}: {}", booking.id(), e.getMessage(), e);
            throw new RuntimeException("Failed to create payment link: " + e.getMessage(), e);
        }
    }

    @Override
    public Payment getPaymentById(Long id) {
        return paymentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment order not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public Payment getPaymentByPaymentId(String paymentId) {
        if (paymentId == null || paymentId.isBlank()) {
            log.warn("Invalid payment link ID");
            throw new IllegalArgumentException("Valid payment link ID is required");
        }

        Payment payment = paymentRepo.findByPaymentLinkId(paymentId);
        if (payment == null) {
            log.warn("Payment not found with link ID: {}", paymentId);
            throw new RuntimeException("Payment not found with link ID: " + paymentId);
        }
        return payment;
    }

    @Override
    @Transactional
    public Boolean proceedPayment(Payment payment, PaymentRequest paymentRequest) {
        PaymentProcessor processor =
                paymentProcessorFactory.getProcessor(
                        payment.getPaymentMethod()
                );

        return processor.process(payment, paymentRequest);
    }
}

