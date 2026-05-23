package com.lamnd.messaging;

import com.lamnd.dto.NotificationDTO;
import com.lamnd.entity.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationEventProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendNotificationEvent(Long bookingId, Long userId, Long salonId) {
        NotificationDTO notification = NotificationDTO.builder()
                .bookingId(bookingId)
                .userId(userId)
                .salonId(salonId)
                .type("BOOKING")
                .description("Your booking has been successfully paid.")
                .build();

        rabbitTemplate.convertAndSend("notification-queue", notification);
    }
}
