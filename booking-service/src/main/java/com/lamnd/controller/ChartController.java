package com.lamnd.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lamnd.dto.SalonDTO;
import com.lamnd.dto.response.BookingResponse;
import com.lamnd.entity.Booking;
import com.lamnd.service.BookingChartService;
import com.lamnd.service.BookingService;
import com.lamnd.service.client.SalonFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookings/chart")
public class ChartController {

    private final BookingChartService bookingChartService;
    private final BookingService bookingService;
    private final SalonFeignClient salonFeignClient;
    private final ObjectMapper objectMapper;

    @GetMapping("/earnings")
    public ResponseEntity<List<Map<String, Object>>> getEarningsChartData(
            @RequestHeader("Authorization") String token) {

        SalonDTO salon = objectMapper
                .convertValue(Objects.requireNonNull(salonFeignClient.getSalonByOwnerId(token).getBody()).data(), SalonDTO.class);
        List<BookingResponse> bookings = bookingService.getBookingsBySalonId(salon.id());

        // generate daily earnings chart data
        List<Map<String, Object>> chartData = bookingChartService.generateDailyEarningsChartData(bookings);

        return ResponseEntity.ok(chartData);
    }

    @GetMapping("/bookings")
    public ResponseEntity<List<Map<String, Object>>> getBookingsChartData(
            @RequestHeader("Authorization") String token) {

        SalonDTO salon = objectMapper
                .convertValue(Objects.requireNonNull(salonFeignClient.getSalonByOwnerId(token).getBody()).data(), SalonDTO.class);
        List<BookingResponse> bookings = bookingService.getBookingsBySalonId(salon.id());


        // generate daily earnings chart data
        List<Map<String, Object>> chartData = bookingChartService.generateDailyBookingsChartData(bookings);

        return ResponseEntity.ok(chartData);
    }
}
