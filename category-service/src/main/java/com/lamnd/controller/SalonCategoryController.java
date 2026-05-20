package com.lamnd.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lamnd.common.ApiResponse;
import com.lamnd.common.BaseController;
import com.lamnd.dto.SalonDTO;
import com.lamnd.dto.request.CategoryCreateRequest;
import com.lamnd.dto.response.CategoryResponse;
import com.lamnd.service.Categoryservice;
import com.lamnd.service.client.SalonFeignClient;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories/salon-owner")
public class SalonCategoryController extends BaseController {
    private final Categoryservice categoryservice;
    private final SalonFeignClient salonFeignClient;
    private final ObjectMapper objectMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createCategory(
            @RequestBody @Valid CategoryCreateRequest createRequest,
            @RequestHeader("Authorization") String token){
        SalonDTO salonDTO = objectMapper
                .convertValue(Objects.requireNonNull(salonFeignClient.getSalonByOwnerId(token).getBody()).data(), SalonDTO.class);

        CategoryResponse savedCategory = categoryservice.createCategory(createRequest, salonDTO);

        return new ResponseEntity<>(createSuccessResponse(savedCategory), HttpStatus.CREATED);
    }

    @GetMapping("/salon/{salonId}/category/{categoryId}")
    public ResponseEntity<ApiResponse<?>> getCategoryByIdAndSalon(
            @PathVariable("salonId") Long salonId,
            @PathVariable("categoryId") Long categoryId) {
        return ResponseEntity.ok(createSuccessResponse(categoryservice.findByIdAndSalonId(categoryId, salonId)));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<?>> deleteCategory(
            @PathVariable("categoryId") Long categoryId,
            @RequestHeader("Authorization") String token)
    {
        SalonDTO salonDTO = objectMapper
                .convertValue(Objects.requireNonNull(salonFeignClient.getSalonByOwnerId(token).getBody()).data(), SalonDTO.class);

        categoryservice.deleteCategoryById(categoryId, salonDTO.id());
        return ResponseEntity.noContent().build();
    }
}
