package com.lamnd.service;

import com.lamnd.dto.response.NotificationResponse;
import com.lamnd.entity.Notification;

import java.util.List;

public interface NotificationService {

    NotificationResponse createNotification(Notification notification);
    List<Notification> getAllNotificationByUserId(Long userId);
    List<Notification> getAllNotificationBySalonId(Long salonId);
    Notification markNotificationAdRead(Long notificationId);
}
