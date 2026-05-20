package com.lamnd.service.client;

import com.lamnd.common.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("USER-SERVICE")
public interface UserFeignClient {

    @GetMapping("/api/v1/users/{userId}")
    ResponseEntity<ApiResponse<?>> getUserById(@PathVariable("userId") Long userId);

    @GetMapping("/api/v1/users/profile")
    ResponseEntity<ApiResponse<?>> getMyInfo(@RequestHeader("Authorization") String token);
}
