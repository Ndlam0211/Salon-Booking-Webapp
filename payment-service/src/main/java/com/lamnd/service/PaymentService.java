package com.lamnd.service;

import com.lamnd.dto.BookingDTO;
import com.lamnd.dto.UserDTO;
import com.lamnd.dto.response.PaymentLinkResponse;
import com.lamnd.dto.response.PaymentResponse;
import com.lamnd.enums.PaymentMethod;

public interface PaymentService {
    PaymentLinkResponse createOrder(UserDTO user,
                                    BookingDTO booking,
                                    PaymentMethod paymentMethod);

    PaymentResponse getPaymentById(Long id);
    PaymentResponse getPaymentByPaymentId(Long paymentId);
    String createStripePaymentLink(UserDTO user,
                                   Long amount,
                                   Long orderId);
}
