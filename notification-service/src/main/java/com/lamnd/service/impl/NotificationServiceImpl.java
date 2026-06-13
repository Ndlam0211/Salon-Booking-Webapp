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
    private final RealTimeCommunicationServiceImpl realTimeCommunicationService;

    @Override
    public NotificationResponse createNotification(Notification notification) {
        Notification savedNotification = notificationRepo.save(notification);

        ApiResponse<?> bookingResponse = bookingFeignClient.getBookingById(
                savedNotification.getBookingId());

        BookingDTO bookingDTO = objectMapper.convertValue(
                bookingResponse.data(), BookingDTO.class);

        NotificationResponse notificationResponse = NotificationMapper.toDTO(savedNotification, bookingDTO);

        realTimeCommunicationService.sendNotification(notificationResponse);

        return notificationResponse;
    }

    @Override
    public List<NotificationResponse> getAllNotificationByUserId(Long userId) {
        List<NotificationResponse> notifications = notificationRepo.findByUserId(userId).stream()
                .map(notification -> {
                    BookingDTO bookingDTO = objectMapper.convertValue(
                            bookingFeignClient.getBookingById(notification.getBookingId()).data(), BookingDTO.class);
                    return NotificationMapper.toDTO(notification, bookingDTO);
                })
                .toList();

        return notifications;
    }

    @Override
    public List<NotificationResponse> getAllNotificationBySalonId(Long salonId) {
        List<NotificationResponse> notifications = notificationRepo.findBySalonId(salonId).stream()
                .map(notification -> {
                    BookingDTO bookingDTO = objectMapper.convertValue(
                            bookingFeignClient.getBookingById(notification.getBookingId()).data(), BookingDTO.class);
                    return NotificationMapper.toDTO(notification, bookingDTO);
                })
                .toList();

        return notifications;
    }

    @Override
    public NotificationResponse markNotificationAdRead(Long notificationId) {
        return notificationRepo.findById(notificationId)
                .map(notification -> {
                    notification.setIsRead(true);

                    BookingDTO bookingDTO = objectMapper.convertValue(
                            bookingFeignClient.getBookingById(notification.getBookingId()).data(), BookingDTO.class);

                    return NotificationMapper.toDTO(notificationRepo.save(notification), bookingDTO);
                })
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + notificationId));
    }
}
