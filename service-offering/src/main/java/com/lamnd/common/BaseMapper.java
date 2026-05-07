package com.lamnd.common;

import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
import java.util.Set;

public interface BaseMapper<E, D, C, U> {
    // Entity to DTO
    D toDTO(E entity);

    // List Entity to List DTO
    List<D> toList(List<E> entities);

    // Set Entity to Set DTO
    Set<D> toSet(Set<E> entities);

    // Create Request to Entity: id, created_at and updated_at are automatically generated, so ignore them
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE) // Ignore null values
    E toEntity(C createRequest);

    // Update Request to Entity
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(U updateRequest,@MappingTarget E entity);
}
