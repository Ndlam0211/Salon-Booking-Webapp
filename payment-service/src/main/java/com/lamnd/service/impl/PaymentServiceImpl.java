package com.lamnd.service.impl;

import com.lamnd.dto.BookingDTO;
import com.lamnd.dto.UserDTO;
import com.lamnd.dto.response.PaymentLinkResponse;
import com.lamnd.dto.response.PaymentResponse;
import com.lamnd.entity.Payment;
import com.lamnd.enums.PaymentMethod;
import com.lamnd.enums.PaymentStatus;
import com.lamnd.mapper.PaymentMapper;
import com.lamnd.repository.PaymentRepo;
import com.lamnd.service.PaymentService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepo paymentRepo;
    private final PaymentMapper paymentMapper;

    @Value("${stripe.api.key}")
    private String stripeSecretKey;

    @Override
    public PaymentLinkResponse createOrder(UserDTO user,
                                           BookingDTO booking,
                                           PaymentMethod paymentMethod) throws StripeException {
        Double amount = booking.totalPrice();

        Payment newOrder = Payment.builder()
                .amount(amount)
                .paymentMethod(paymentMethod)
                .bookingId(booking.id())
                .salonId(booking.salonId())
                .build();

        Payment savedOrder = paymentRepo.save(newOrder);

        PaymentLinkResponse paymentLinkResponse = new PaymentLinkResponse();

        if (paymentMethod.equals(PaymentMethod.STRIPE)) {
            String paymentUrl = createStripePaymentLink(user, savedOrder.getAmount(), savedOrder.getId());

            paymentLinkResponse.setPaymentLinkUrl(paymentUrl);
        }

        return paymentLinkResponse;
    }

    @Override
    public PaymentResponse getPaymentById(Long id) {
        Payment payment =  paymentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment order not found"));

        return paymentMapper.toResponse(payment);
    }

    @Override
    public Payment getPaymentByPaymentId(String paymentId) {
        return paymentRepo.findByPaymentLinkId(paymentId);
    }

    @Override
    public String createStripePaymentLink(UserDTO user, Double amount, Long orderId) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:3000/payment-success/"+orderId)
                .setCancelUrl("http://localhost:3000/paymen/cancel")
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("usd")
                                .setUnitAmount((long) (amount*100))
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName("salon appointment booking")
                                        .build())
                                .build()
                        ).build())
                .build();

        Session session = Session.create(params);

        return session.getUrl();
    }

    @Override
    public Boolean proceedPayment(Payment payment, String paymentId, String paymentLinkId) {
        if (payment.getStatus().equals(PaymentStatus.PENDING)) {
            if (payment.getPaymentMethod().equals(PaymentMethod.STRIPE)) {
                payment.setStatus(PaymentStatus.SUCCESS);
                paymentRepo.save(payment);
                return true;
            }
        }

        return false;
    }
}
