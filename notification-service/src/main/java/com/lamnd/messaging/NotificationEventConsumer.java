package com.lamnd.messaging;

import com.lamnd.entity.Notification;
import com.lamnd.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class NotificationEventConsumer {

    private final NotificationService notificationService;

    @RabbitListener(queues = "notification-queue")
    public void notificationListener(Notification notification) {
        notificationService.createNotification(notification);
    }
}
