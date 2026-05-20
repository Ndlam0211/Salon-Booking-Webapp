package com.lamnd.controller;

import com.lamnd.dto.response.NotificationResponse;
import com.lamnd.entity.Notification;
import com.lamnd.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<NotificationResponse> createNotification(
            @RequestBody Notification notification) {

        NotificationResponse response = notificationService.createNotification(notification);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponse>> getNotificationsByUserId(
            @PathVariable("userId") Long userId) {
        List<NotificationResponse> notifications = notificationService.getAllNotificationByUserId(userId);
        return ResponseEntity.ok(notifications);
    }

    @PutMapping("/{notificationId}/read")
    public ResponseEntity<NotificationResponse> markNotificationAsRead(
            @PathVariable("notificationId") Long notificationId) {

        NotificationResponse response = notificationService.markNotificationAdRead(notificationId);
        return ResponseEntity.ok(response);
    }
}
