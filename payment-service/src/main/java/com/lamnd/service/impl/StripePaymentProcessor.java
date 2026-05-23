package com.lamnd.service.impl;

import com.lamnd.dto.request.CreatePaymentRequest;
import com.lamnd.dto.request.PaymentRequest;
import com.lamnd.entity.Payment;
import com.lamnd.enums.PaymentMethod;
import com.lamnd.enums.PaymentStatus;
import com.lamnd.messaging.BookingEventProducer;
import com.lamnd.messaging.NotificationEventProducer;
import com.lamnd.repository.PaymentRepo;
import com.lamnd.service.PaymentProcessor;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StripePaymentProcessor implements PaymentProcessor {

    private final PaymentRepo paymentRepo;
    private final BookingEventProducer bookingEventProducer;
    private final NotificationEventProducer notificationEventProducer;

    @Value("${payment.stripe.api.key}")
    private String stripeSecretKey;

    @Override
    public PaymentMethod getPaymentMethod() {
        return PaymentMethod.STRIPE;
    }

    @Override
    public String createPaymentLink(
            CreatePaymentRequest request
    ) throws StripeException {

        Stripe.apiKey = stripeSecretKey;

        SessionCreateParams params =
                SessionCreateParams.builder()
                        .addPaymentMethodType(
                                SessionCreateParams.PaymentMethodType.CARD
                        )
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl(
                                request.getReturnUrl()
                                        + request.getOrderId()
                                        + "?session_id={CHECKOUT_SESSION_ID}"
                        )
                        .setCancelUrl(request.getCancelUrl())
                        .addLineItem(
                                SessionCreateParams.LineItem.builder()
                                        .setQuantity(1L)
                                        .setPriceData(
                                                SessionCreateParams.LineItem
                                                        .PriceData.builder()
                                                        .setCurrency("vnd")
                                                        .setUnitAmount(
                                                                request.getAmount()
                                                                        .longValue()
                                                        )
                                                        .setProductData(
                                                                SessionCreateParams
                                                                        .LineItem
                                                                        .PriceData
                                                                        .ProductData
                                                                        .builder()
                                                                        .setName(
                                                                                request.getDescription()
                                                                        )
                                                                        .build()
                                                        )
                                                        .build()
                                        )
                                        .build()
                        )
                        .build();

        Session session = Session.create(params);

        return session.getUrl();
    }

    @Override
    @Transactional
    public boolean process(Payment payment, PaymentRequest request) {

        if (payment.getStatus().equals(PaymentStatus.SUCCESS)) {
            return true;
        }

        // Verify Stripe session
        Session session = null;
        try {
            session = Session.retrieve(request.getSessionId());
        } catch (StripeException e) {
            throw new RuntimeException("Failed to retrieve Stripe session", e);
        }

        if (!"paid".equals(session.getPaymentStatus())) {
            return false;
        }

        payment.setStatus(PaymentStatus.SUCCESS);

        paymentRepo.save(payment);

        bookingEventProducer.sendBookingEvent(payment);

        notificationEventProducer.sendNotificationEvent(
                payment.getBookingId(),
                payment.getUserId(),
                payment.getSalonId()
        );

        return true;
    }
}
