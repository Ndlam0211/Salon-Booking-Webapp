package com.lamnd.mapper;

import com.lamnd.common.BaseMapper;
import com.lamnd.dto.request.SalonCreateRequest;
import com.lamnd.dto.request.SalonUpdateRequest;
import com.lamnd.dto.response.SalonResponse;
import com.lamnd.entity.Salon;
import org.mapstruct.*;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface SalonMapper extends BaseMapper<Salon, SalonResponse, SalonCreateRequest, SalonUpdateRequest> {

    @Override
    @Mapping(target = "ownerId", ignore = true) // Ignore ownerId when mapping to DTO
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE) // Ignore null values
    Salon toEntity(SalonCreateRequest createRequest);

    @Override
    @Mapping(target = "ownerId", ignore = true) // Ignore ownerId when mapping to DTO
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(SalonUpdateRequest updateRequest,@MappingTarget Salon entity);
}
