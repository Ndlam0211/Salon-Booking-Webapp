package com.lamnd.service;

import com.lamnd.dto.request.CreatePaymentRequest;
import com.lamnd.dto.request.PaymentRequest;
import com.lamnd.entity.Payment;
import com.lamnd.enums.PaymentMethod;

public interface PaymentProcessor {

    PaymentMethod getPaymentMethod();

    String createPaymentLink(CreatePaymentRequest request) throws Exception;

    boolean process(
            Payment payment,
            PaymentRequest request
    );
}
