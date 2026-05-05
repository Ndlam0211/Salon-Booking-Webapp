package com.lamnd.controller;

import com.lamnd.common.ApiResponse;
import com.lamnd.common.BaseController;
import com.lamnd.service.Categoryservice;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController extends BaseController {

    private final Categoryservice categoryservice;

    @GetMapping("/salon/{salonId}")
    public ResponseEntity<ApiResponse<?>> getAllCategoriesBySalon(@PathVariable("salonId") Long salonId) {
        return ResponseEntity.ok(createSuccessResponse(categoryservice.getAllCategoriesBySalon(salonId)));
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<?>> getCategoryById(@PathVariable("categoryId") Long categoryId) {
        return ResponseEntity.ok(createSuccessResponse(categoryservice.getCategoryById(categoryId)));
    }
}
