package com.lamnd.mapper;

import com.lamnd.common.BaseMapper;
import com.lamnd.dto.request.CategoryCreateRequest;
import com.lamnd.dto.request.CategoryUpdateRequest;
import com.lamnd.dto.response.CategoryResponse;
import com.lamnd.enitity.Category;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CategoryMapper extends BaseMapper<Category, CategoryResponse, CategoryCreateRequest, CategoryUpdateRequest> {

    @Override
    @Mapping(target = "salonId", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE) // Ignore null values
    Category toEntity(CategoryCreateRequest createRequest);

    @Override
    @Mapping(target = "salonId", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(CategoryUpdateRequest updateRequest,@MappingTarget Category entity);
}
