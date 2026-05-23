package com.lamnd.service;

import com.lamnd.dto.BookingDTO;
import com.lamnd.dto.UserDTO;
import com.lamnd.dto.request.PaymentRequest;
import com.lamnd.dto.response.PaymentLinkResponse;
import com.lamnd.entity.Payment;
import com.lamnd.enums.PaymentMethod;
import com.stripe.exception.StripeException;

public interface PaymentService {
    PaymentLinkResponse createOrder(UserDTO user,
                                    BookingDTO booking,
                                    PaymentMethod paymentMethod) throws StripeException;

    Payment getPaymentById(Long id);
    Payment getPaymentByPaymentId(String paymentId);
    Boolean proceedPayment(Payment payment, PaymentRequest paymentRequest);
}
