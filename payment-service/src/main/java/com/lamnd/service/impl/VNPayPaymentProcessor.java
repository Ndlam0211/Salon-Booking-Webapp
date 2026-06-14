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
import com.lamnd.utils.VNPayUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;

@Service
@RequiredArgsConstructor
public class VNPayPaymentProcessor implements PaymentProcessor {

    private final PaymentRepo paymentRepo;
    private final BookingEventProducer bookingEventProducer;
    private final NotificationEventProducer notificationEventProducer;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final String DEFAULT_IP = "127.0.0.1";
    private static final String VNP_VERSION = "2.1.0";
    private static final String VNP_COMMAND = "pay";
    private static final String VNP_ORDER_TYPE = "other";
    private static final String VNP_LOCALE = "vn";
    private static final String VNP_CURRENCY = "VND";
    private static final String VNP_SUCCESS_CODE = "00";

    @Value("${payment.vnpay.tmn-code}")
    private String tmnCode;

    @Value("${payment.vnpay.hash-secret}")
    private String hashSecret;

    @Value("${payment.vnpay.pay-url}")
    private String payUrl;

    @Override
    public PaymentMethod getPaymentMethod() {
        return PaymentMethod.VNPAY;
    }

    @Override
    public String createPaymentLink(
            CreatePaymentRequest request
    ) throws Exception {

        // Validate input
        if (request == null || request.getAmount() == null || request.getOrderId() == null) {
            throw new IllegalArgumentException("Invalid payment request: amount and orderId are required");
        }

        Map<String, String> vnpParams = new TreeMap<>();

        vnpParams.put("vnp_Version", VNP_VERSION);
        vnpParams.put("vnp_Command", VNP_COMMAND);
        vnpParams.put("vnp_TmnCode", tmnCode);

        long amount = request.getAmount().longValue() * 100;
        vnpParams.put("vnp_Amount", String.valueOf(amount));

        vnpParams.put("vnp_TxnRef", String.valueOf(request.getOrderId()));
        vnpParams.put("vnp_OrderInfo", request.getDescription() != null ? request.getDescription() : "");
        vnpParams.put("vnp_OrderType", VNP_ORDER_TYPE);
        vnpParams.put("vnp_ReturnUrl", request.getReturnUrl());
        vnpParams.put("vnp_IpAddr", DEFAULT_IP);
        vnpParams.put("vnp_Locale", VNP_LOCALE);
        vnpParams.put("vnp_CurrCode", VNP_CURRENCY);
        vnpParams.put("vnp_CreateDate", LocalDateTime.now().format(DATE_FORMATTER));

        String queryUrl = VNPayUtil.buildQuery(vnpParams);
        String hashData = VNPayUtil.hashAllFields(vnpParams);
        String secureHash = VNPayUtil.hmacSHA512(hashSecret, hashData);

        return payUrl + "?" + queryUrl + "&vnp_SecureHash=" + secureHash;
    }

    @Override
    @Transactional
    public boolean process(Payment payment, PaymentRequest request) {

        if (payment.getStatus().equals(PaymentStatus.SUCCESS)) {
            return true;
        }

        // Validate request metadata
        if (request.getMetadata() == null || request.getMetadata().isEmpty()) {
            return false;
        }

        String responseCode = (String) request.getMetadata().get("vnp_ResponseCode");

        if (!VNP_SUCCESS_CODE.equals(responseCode)) {
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

