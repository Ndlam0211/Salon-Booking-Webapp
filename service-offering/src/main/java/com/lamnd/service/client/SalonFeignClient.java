package com.lamnd.service.client;

import com.lamnd.common.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("SALON-SERVICE")
public interface SalonFeignClient {

    @GetMapping("api/v1/salons/owner")
    ResponseEntity<ApiResponse<?>> getSalonByOwnerId(@RequestHeader("Authorization") String token);
}
