package com.lamnd.service.client;

import com.lamnd.common.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "USER-SERVICE")
public interface UserFeignClient {

    @GetMapping("/{userId}")
    ResponseEntity<ApiResponse<?>> getUserById(@PathVariable("userId") Long userId);

    @GetMapping("/profile")
    ResponseEntity<ApiResponse<?>> getMyInfo(@RequestHeader("Authorization") String token);
}
