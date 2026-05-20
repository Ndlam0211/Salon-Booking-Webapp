package com.lamnd.mapper;

import com.lamnd.dto.BookingDTO;
import com.lamnd.dto.response.NotificationResponse;
import com.lamnd.entity.Notification;

public class NotificationMapper {

    public static NotificationResponse toDTO(
            Notification notification,
            BookingDTO bookingDTO) {

        return NotificationResponse.builder()
                .id(notification.getId())
                .type(notification.getType())
                .description(notification.getDescription())
                .isRead(notification.getIsRead())
                .userId(notification.getUserId())
                .bookingId(notification.getBookingId())
                .salonId(notification.getSalonId())
                .booking(bookingDTO)
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
