package com.lamnd.service.impl;

import com.lamnd.dto.response.NotificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RealTimeCommunicationServiceImpl {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendNotification(NotificationResponse notificationResponse) {
        messagingTemplate.convertAndSend("/notification/user/" + notificationResponse.userId(),
                notificationResponse);

        messagingTemplate.convertAndSend("/notification/salon/"+notificationResponse.salonId(),
                notificationResponse);
    }
}
