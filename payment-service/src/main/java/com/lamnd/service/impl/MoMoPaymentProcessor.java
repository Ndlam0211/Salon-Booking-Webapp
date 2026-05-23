package com.lamnd.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lamnd.dto.request.CreatePaymentRequest;
import com.lamnd.dto.request.PaymentRequest;
import com.lamnd.entity.Payment;
import com.lamnd.enums.PaymentMethod;
import com.lamnd.enums.PaymentStatus;
import com.lamnd.messaging.BookingEventProducer;
import com.lamnd.messaging.NotificationEventProducer;
import com.lamnd.repository.PaymentRepo;
import com.lamnd.service.PaymentProcessor;
import com.lamnd.utils.MoMoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MoMoPaymentProcessor implements PaymentProcessor {

    private final PaymentRepo paymentRepo;
    private final BookingEventProducer bookingEventProducer;
    private final NotificationEventProducer notificationEventProducer;
    private final RestTemplate restTemplate;

    @Value("${payment.momo.partner-code}")
    private String partnerCode;

    @Value("${payment.momo.access-key}")
    private String accessKey;

    @Value("${payment.momo.secret-key}")
    private String secretKey;

    @Value("${payment.momo.request-type}")
    private String requestType;

    @Value("${payment.momo.endpoint}")
    private String endpoint;

    @Override
    public PaymentMethod getPaymentMethod() {
        return PaymentMethod.MOMO;
    }

    @Override
    public String createPaymentLink(
            CreatePaymentRequest request
    ) throws Exception {

        String requestId =
                UUID.randomUUID().toString();

        String orderId =
                String.valueOf(request.getOrderId());

        String rawHash =
                "accessKey=" + accessKey +
                        "&amount=" + request.getAmount().longValue() +
                        "&extraData=" +
                        "&ipnUrl=" + request.getReturnUrl() +
                        "&orderId=" + orderId +
                        "&orderInfo=" + request.getDescription() +
                        "&partnerCode=" + partnerCode +
                        "&redirectUrl=" + request.getReturnUrl() +
                        "&requestId=" + requestId +
                        "&requestType=" + requestType;

        String signature =
                MoMoUtil.hmacSHA256(
                        secretKey,
                        rawHash
                );

        Map<String, Object> payload = new HashMap<>();

        payload.put("partnerCode", partnerCode);
        payload.put("requestId", requestId);
        payload.put("amount", request.getAmount().longValue());
        payload.put("orderId", orderId);
        payload.put("orderInfo", request.getDescription());
        payload.put("redirectUrl", request.getReturnUrl());
        payload.put("ipnUrl", request.getReturnUrl());
        payload.put("requestType", requestType);
        payload.put("signature", signature);
        payload.put("accessKey", accessKey);
        payload.put("extraData", "");
        payload.put("lang", "vi");

        // Send request to MoMo API
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(payload, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                endpoint,
                httpEntity,
                Map.class
        );

        Map<String, Object> responseBody = response.getBody();

        if (responseBody == null) {
            throw new Exception("MoMo API returned empty response");
        }

        Integer resultCode = (Integer) responseBody.get("resultCode");

        if (resultCode != null && resultCode == 0) {
            String payUrl = (String) responseBody.get("payUrl");
            if (payUrl != null) {
                return payUrl;
            }
        }

        String message = (String) responseBody.get("message");
        throw new Exception("Failed to create MoMo payment link: " + (message != null ? message : "Unknown error"));
    }

    @Override
    @Transactional
    public boolean process(
            Payment payment,
            PaymentRequest request
    ) {
        // Check if payment already succeeded
        if (payment.getStatus().equals(PaymentStatus.SUCCESS)) {
            return true;
        }

        // Extract MoMo callback data from request metadata
        if (request.getMetadata() == null || request.getMetadata().isEmpty()) {
            return false;
        }

        Integer resultCode = null;
        if (request.getMetadata().get("resultCode") instanceof Integer) {
            resultCode = (Integer) request.getMetadata().get("resultCode");
        } else if (request.getMetadata().get("resultCode") instanceof String) {
            try {
                resultCode = Integer.parseInt((String) request.getMetadata().get("resultCode"));
            } catch (NumberFormatException e) {
                return false;
            }
        }

        // resultCode 0 means transaction successful
        if (resultCode == null || resultCode != 0) {
            return false;
        }

        // Update payment status to SUCCESS
        payment.setStatus(PaymentStatus.SUCCESS);
        paymentRepo.save(payment);

        // Send booking event to booking service
        bookingEventProducer.sendBookingEvent(payment);

        // Send notification event
        notificationEventProducer.sendNotificationEvent(
                payment.getBookingId(),
                payment.getUserId(),
                payment.getSalonId()
        );

        return true;
    }
}
