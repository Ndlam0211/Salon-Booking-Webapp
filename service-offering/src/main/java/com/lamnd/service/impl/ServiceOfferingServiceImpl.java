package com.lamnd.service.impl;

import com.lamnd.dto.CategoryDTO;
import com.lamnd.dto.SalonDTO;
import com.lamnd.dto.request.ServiceOfferingCreateRequest;
import com.lamnd.dto.request.ServiceOfferingUpdateRequest;
import com.lamnd.dto.response.ServiceOfferingResponse;
import com.lamnd.service.ServiceOfferingService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ServiceOfferingServiceImpl implements ServiceOfferingService {
    @Override
    public ServiceOfferingResponse createServiceOffering(ServiceOfferingCreateRequest createRequest, SalonDTO salon, CategoryDTO category) {
        return null;
    }

    @Override
    public ServiceOfferingResponse updateServiceOffering(Long id, ServiceOfferingUpdateRequest updateRequest) {
        return null;
    }

    @Override
    public Set<ServiceOfferingResponse> getAllServiceOfferingsBySalonId(Long salonId, Long categoryId) {
        return Set.of();
    }

    @Override
    public Set<ServiceOfferingResponse> getServiceOfferingsByIds(Set<Long> ids) {
        return Set.of();
    }
}
