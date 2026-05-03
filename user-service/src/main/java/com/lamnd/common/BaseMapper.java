package com.lamnd.common;

import com.lamnd.annotation.BaseMapperAnnotation;
import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

public interface BaseMapper<E, D, C, U> {
    // Entity to DTO
    D toDTO(E entity);

    // List Entity to List DTO
    List<D> toList(List<E> entities);

    // Create Request to Entity: id, created_at and updated_at are automatically generated, so ignore them
    @BaseMapperAnnotation
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE) // Ignore null values
    E toEntity(C createRequest);

    // Update Request to Entity
    @BaseMapperAnnotation
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(U updateRequest,@MappingTarget E entity);
}
