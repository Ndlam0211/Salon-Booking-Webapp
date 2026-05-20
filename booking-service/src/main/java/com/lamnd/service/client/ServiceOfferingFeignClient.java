package com.lamnd.service.client;

import com.lamnd.common.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Set;

@FeignClient("SERVICE-OFFERING")
public interface ServiceOfferingFeignClient {

    @GetMapping("/api/v1/service-offerings/salon/{salonId}/list/{ids}")
    ResponseEntity<ApiResponse<?>> getServiceOfferingsByIds(
            @PathVariable("ids") Set<Long> ids,
            @PathVariable("salonId") Long salonId);
}
