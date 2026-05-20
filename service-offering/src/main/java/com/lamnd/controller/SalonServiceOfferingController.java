package com.lamnd.controller;

import com.lamnd.common.ApiResponse;
import com.lamnd.common.BaseController;
import com.lamnd.dto.CategoryDTO;
import com.lamnd.dto.SalonDTO;
import com.lamnd.dto.request.ServiceOfferingCreateRequest;
import com.lamnd.dto.request.ServiceOfferingUpdateRequest;
import com.lamnd.dto.response.ServiceOfferingResponse;
import com.lamnd.service.ServiceOfferingService;
import com.lamnd.service.client.CategoryFeignClient;
import com.lamnd.service.client.SalonFeignClient;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/service-offerings/salon-owner")
public class SalonServiceOfferingController extends BaseController {

    private final ServiceOfferingService serviceOfferingService;
    private final CategoryFeignClient categoryFeignClient;
    private final SalonFeignClient salonFeignClient;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createServiceOffering(
            @RequestBody @Valid ServiceOfferingCreateRequest createRequest,
            @RequestHeader("Authorization") String token
    ) {
        SalonDTO salon = (SalonDTO) Objects.requireNonNull(salonFeignClient.getSalonByOwnerId(token).getBody()).data();

        CategoryDTO category = (CategoryDTO) Objects.requireNonNull(categoryFeignClient.getCategoryByIdAndSalon(createRequest.categoryId(), salon.id()).getBody()).data();

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
