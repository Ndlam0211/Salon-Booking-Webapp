package com.lamnd.messaging;

import com.lamnd.entity.Payment;
import com.lamnd.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BookingEventConsumer {

    private final BookingService bookingService;

    @RabbitListener(queues = "booking-queue")
    public void bookingListener(Payment payment) {
        bookingService.updateBookingStatus(payment);
    }
}
