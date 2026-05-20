package com.lamnd.controller;

import com.lamnd.dto.response.NotificationResponse;
import com.lamnd.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications/salon-owner")
@RequiredArgsConstructor
public class SalonNotificationController {
    private final NotificationService notificationService;

    @GetMapping("/salon/{salonId}")
    public ResponseEntity<List<NotificationResponse>> getNotificationsByUSalonId(
            @PathVariable("salonId") Long salonId) {
        List<NotificationResponse> notifications = notificationService.getAllNotificationBySalonId(salonId);
        return ResponseEntity.ok(notifications);
    }
}
