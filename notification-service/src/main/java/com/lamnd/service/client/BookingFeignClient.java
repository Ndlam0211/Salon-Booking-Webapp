package com.lamnd.service.client;

import com.lamnd.common.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("BOOKING-SERVICE")
public interface BookingFeignClient {

    @GetMapping("/api/v1/bookings/{id}")
    ApiResponse<?> getBookingById(@PathVariable("id") Long id);
}
