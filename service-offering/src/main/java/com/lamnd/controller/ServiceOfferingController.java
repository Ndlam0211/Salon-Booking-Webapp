package com.lamnd.controller;

import com.lamnd.common.ApiResponse;
import com.lamnd.common.BaseController;
import com.lamnd.dto.response.ServiceOfferingResponse;
import com.lamnd.service.ServiceOfferingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/service-offerings")
public class ServiceOfferingController extends BaseController {

    private final ServiceOfferingService serviceOfferingService;

    @GetMapping("/salon/{salonId}")
    public ResponseEntity<ApiResponse<?>> getServiceOfferingsBySalonId(@PathVariable("salonId") Long salonId, @RequestParam(required = false) Long categoryId) {
        Set<ServiceOfferingResponse> serviceOfferings = serviceOfferingService.getAllServiceOfferingsBySalonId(salonId, categoryId);

        return ResponseEntity.ok(createSuccessResponse(serviceOfferings));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getServiceOfferingsById(@PathVariable("id") Long id) {
        ServiceOfferingResponse serviceOffering = serviceOfferingService.getServiceOfferingById(id);

        return ResponseEntity.ok(createSuccessResponse(serviceOffering));
    }

    @GetMapping("/list/{ids}")
    public ResponseEntity<ApiResponse<?>> getServiceOfferingsByIds(@PathVariable("ids") Set<Long> ids) {
        Set<ServiceOfferingResponse> serviceOfferings = serviceOfferingService.getServiceOfferingsByIds(ids);

        return ResponseEntity.ok(createSuccessResponse(serviceOfferings));
    }
}
