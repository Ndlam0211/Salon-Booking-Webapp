package com.lamnd.service.client;

import com.lamnd.common.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("CATEGORY-SERVICE")
public interface CategoryFeignClient {

    @GetMapping("/api/v1/categories/{categoryId}")
    ResponseEntity<ApiResponse<?>> getCategoryById(@PathVariable("categoryId") Long categoryId);

    @GetMapping("/api/v1/categories/salon-owner/salon/{salonId}/category/{categoryId}")
    ResponseEntity<ApiResponse<?>> getCategoryByIdAndSalon(
            @PathVariable("categoryId") Long categoryId,
            @PathVariable("salonId") Long salonId);
}
