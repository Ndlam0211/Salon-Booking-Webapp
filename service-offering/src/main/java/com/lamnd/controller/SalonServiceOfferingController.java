package com.lamnd.controller;

import com.lamnd.common.ApiResponse;
import com.lamnd.common.BaseController;
import com.lamnd.dto.CategoryDTO;
import com.lamnd.dto.SalonDTO;
import com.lamnd.dto.request.ServiceOfferingCreateRequest;
import com.lamnd.dto.request.ServiceOfferingUpdateRequest;
import com.lamnd.dto.response.ServiceOfferingResponse;
import com.lamnd.service.ServiceOfferingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/service-offerings/salon-owner")
public class SalonServiceOfferingController extends BaseController {

    private final ServiceOfferingService serviceOfferingService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createServiceOffering(
            @RequestBody @Valid ServiceOfferingCreateRequest createRequest
    ) {
        SalonDTO salon = SalonDTO.builder()
                .id(1L) // TODO: Get salon ID from authenticated user
                .build();

        CategoryDTO category = CategoryDTO.builder()
                .id(1L) // TODO: Get category ID from category service by openfeign client
                .build();

        ServiceOfferingResponse serviceOffering = serviceOfferingService.createServiceOffering(createRequest, salon, category);

        return new ResponseEntity<>(createSuccessResponse(serviceOffering), HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> createServiceOffering(
            @RequestBody @Valid ServiceOfferingUpdateRequest updateRequest,
            @PathVariable("id") Long id
    ) {
        ServiceOfferingResponse updatedServiceOffering = serviceOfferingService.updateServiceOffering(id, updateRequest);

        return ResponseEntity.ok(createSuccessResponse(updatedServiceOffering));
    }
}
