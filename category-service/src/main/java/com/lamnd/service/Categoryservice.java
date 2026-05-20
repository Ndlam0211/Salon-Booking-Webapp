package com.lamnd.service;

import com.lamnd.dto.SalonDTO;
import com.lamnd.dto.request.CategoryCreateRequest;
import com.lamnd.dto.response.CategoryResponse;

import java.util.Set;

public interface Categoryservice {
    CategoryResponse createCategory(CategoryCreateRequest createRequest, SalonDTO salon);
    Set<CategoryResponse> getAllCategoriesBySalon(Long salonId);
    CategoryResponse getCategoryById(Long categoryId);
    void deleteCategoryById(Long categoryId, Long salonId);
    CategoryResponse findByIdAndSalonId(Long categoryId, Long salonId);
}
