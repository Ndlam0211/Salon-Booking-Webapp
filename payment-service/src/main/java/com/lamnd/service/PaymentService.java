package com.lamnd.service;

import com.lamnd.dto.BookingDTO;
import com.lamnd.dto.UserDTO;
import com.lamnd.dto.response.PaymentLinkResponse;
import com.lamnd.dto.response.PaymentResponse;
import com.lamnd.entity.Payment;
import com.lamnd.enums.PaymentMethod;
import com.stripe.exception.StripeException;

public interface PaymentService {
    PaymentLinkResponse createOrder(UserDTO user,
                                    BookingDTO booking,
                                    PaymentMethod paymentMethod) throws StripeException;

    PaymentResponse getPaymentById(Long id);
    Payment getPaymentByPaymentId(String paymentId);
    String createStripePaymentLink(UserDTO user,
                                   Double amount,
                                   Long orderId) throws StripeException;
    Boolean proceedPayment(Payment payment, String paymentId, String paymentLinkId);
}
