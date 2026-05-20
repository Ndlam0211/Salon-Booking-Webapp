package com.lamnd.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lamnd.common.ApiResponse;
import com.lamnd.dto.BookingDTO;
import com.lamnd.dto.response.NotificationResponse;
import com.lamnd.entity.Notification;
import com.lamnd.mapper.NotificationMapper;
import com.lamnd.repository.NotificationRepo;
import com.lamnd.service.NotificationService;
import com.lamnd.service.client.BookingFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepo notificationRepo;
    private final BookingFeignClient bookingFeignClient;
    private final ObjectMapper objectMapper;

    @Override
    public NotificationResponse createNotification(Notification notification) {
        Notification savedNotification = notificationRepo.save(notification);

        ApiResponse<?> bookingResponse = bookingFeignClient.getBookingById(
                savedNotification.getBookingId());

        BookingDTO bookingDTO = objectMapper.convertValue(
                bookingResponse.data(), BookingDTO.class);

        return NotificationMapper.toDTO(
                savedNotification,
                bookingDTO);
    }

    @Override
    public List<Notification> getAllNotificationByUserId(Long userId) {
        return notificationRepo.findByUserId(userId);
    }

    @Override
    public List<Notification> getAllNotificationBySalonId(Long salonId) {
        return notificationRepo.findBySalonId(salonId);
    }

    @Override
    public Notification markNotificationAdRead(Long notificationId) {
        return notificationRepo.findById(notificationId)
                .map(notification -> {
                    notification.setIsRead(true);
                    return notificationRepo.save(notification);
                })
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + notificationId));
    }
}
