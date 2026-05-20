package com.lamnd.service.impl;

import com.lamnd.dto.response.NotificationResponse;
import com.lamnd.entity.Notification;
import com.lamnd.repository.NotificationRepo;
import com.lamnd.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepo notificationRepo;

    @Override
    public NotificationResponse createNotification(Notification notification) {


        return null;
    }

    @Override
    public List<Notification> getAllNotificationByUserId(Long userId) {
        return List.of();
    }

    @Override
    public List<Notification> getAllNotificationBySalonId(Long salonId) {
        return List.of();
    }

    @Override
    public Notification markNotificationAdRead(Long notificationId) {
        return null;
    }
}
