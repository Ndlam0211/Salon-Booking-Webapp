package com.lamnd.controller;

import com.lamnd.common.ApiResponse;
import com.lamnd.common.BaseController;
import com.lamnd.dto.SalonDTO;
import com.lamnd.dto.request.CategoryCreateRequest;
import com.lamnd.dto.response.CategoryResponse;
import com.lamnd.service.Categoryservice;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories/salon-owner")
public class SalonCategoryController extends BaseController {
    private final Categoryservice categoryservice;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createCategory(@RequestBody @Valid CategoryCreateRequest createRequest) {
        SalonDTO salonDTO = SalonDTO.builder()
                .id(1L)
                .build();

        CategoryResponse savedCategory = categoryservice.createCategory(createRequest, salonDTO);

        return new ResponseEntity<>(createSuccessResponse(savedCategory), HttpStatus.CREATED);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<?>> deleteCategory(@PathVariable("categoryId") Long categoryId) {
        SalonDTO salonDTO = SalonDTO.builder()
                .id(1L)
                .build();

        categoryservice.deleteCategoryById(categoryId, salonDTO.id());
        return ResponseEntity.noContent().build();
    }
}
