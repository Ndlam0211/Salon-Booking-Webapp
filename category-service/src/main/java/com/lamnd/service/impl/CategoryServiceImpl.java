package com.lamnd.service.impl;

import com.lamnd.dto.SalonDTO;
import com.lamnd.dto.request.CategoryCreateRequest;
import com.lamnd.dto.response.CategoryResponse;
import com.lamnd.enitity.Category;
import com.lamnd.exception.ResourceNotFoundException;
import com.lamnd.mapper.CategoryMapper;
import com.lamnd.repository.CategoryRepo;
import com.lamnd.service.Categoryservice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements Categoryservice {

    private final CategoryRepo categoryRepo;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponse createCategory(CategoryCreateRequest createRequest, SalonDTO salon) {
        Category category = categoryMapper.toEntity(createRequest);
        category.setSalonId(salon.id());

        return categoryMapper.toDTO(categoryRepo.save(category));
    }

    @Override
    public Set<CategoryResponse> getAllCategoriesBySalon(Long salonId) {
        Set<Category> categories = categoryRepo.findAllBySalonId(salonId);

        Set<CategoryResponse> categoryResponses = categories.stream()
                .map(categoryMapper::toDTO)
                .collect(Collectors.toSet());

        return categoryResponses;
    }

    @Override
    public CategoryResponse getCategoryById(Long categoryId) {
        Category existingCategory = findCategoryById(categoryId);

        return categoryMapper.toDTO(existingCategory);
    }

    @Override
    public void deleteCategoryById(Long categoryId, Long salonId) {
        Category existingCategory = findCategoryById(categoryId);

        if (!existingCategory.getSalonId().equals(salonId)) {
            throw new RuntimeException("You don't have permission to delete this category");
        }

        categoryRepo.deleteById(categoryId);
    }

    private Category findCategoryById(Long categoryId) {
        return categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
    }
}
