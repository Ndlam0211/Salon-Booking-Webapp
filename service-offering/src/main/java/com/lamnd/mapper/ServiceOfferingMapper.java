package com.lamnd.mapper;

import com.lamnd.common.BaseMapper;
import com.lamnd.dto.request.ServiceOfferingCreateRequest;
import com.lamnd.dto.request.ServiceOfferingUpdateRequest;
import com.lamnd.dto.response.ServiceOfferingResponse;
import com.lamnd.entity.ServiceOffering;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ServiceOfferingMapper extends BaseMapper<ServiceOffering, ServiceOfferingResponse, ServiceOfferingCreateRequest, ServiceOfferingUpdateRequest> {

    @Override
    @Mapping(target = "categoryId", ignore = true)
    @Mapping(target = "salonId", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE) // Ignore null values
    ServiceOffering toEntity(ServiceOfferingCreateRequest createRequest);

    @Override
    @Mapping(target = "categoryId", ignore = true) // Ignore categoryId during update
    @Mapping(target = "salonId", ignore = true) // Ignore salonId during update
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(ServiceOfferingUpdateRequest updateRequest,@MappingTarget ServiceOffering entity);
}
