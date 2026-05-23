package com.lamnd.messaging;

import com.lamnd.entity.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingEventProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendBookingEvent(Payment payment) {
        rabbitTemplate.convertAndSend("booking-queue", payment);
    }
}
