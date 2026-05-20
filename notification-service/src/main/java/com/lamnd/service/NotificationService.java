package com.lamnd.service;

import com.lamnd.dto.response.NotificationResponse;
import com.lamnd.entity.Notification;

import java.util.List;

public interface NotificationService {

    NotificationResponse createNotification(Notification notification);
    List<NotificationResponse> getAllNotificationByUserId(Long userId);
    List<NotificationResponse> getAllNotificationBySalonId(Long salonId);
    NotificationResponse markNotificationAdRead(Long notificationId);
}
