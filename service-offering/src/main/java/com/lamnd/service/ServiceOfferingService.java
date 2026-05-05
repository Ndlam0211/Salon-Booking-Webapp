package com.lamnd.service;

import com.lamnd.dto.CategoryDTO;
import com.lamnd.dto.SalonDTO;
import com.lamnd.dto.request.ServiceOfferingCreateRequest;
import com.lamnd.dto.request.ServiceOfferingUpdateRequest;
import com.lamnd.dto.response.ServiceOfferingResponse;

import java.util.Set;

public interface ServiceOfferingService {

    ServiceOfferingResponse createServiceOffering(ServiceOfferingCreateRequest createRequest,
                                                  SalonDTO salon,
                                                  CategoryDTO category);

    ServiceOfferingResponse updateServiceOffering(Long id, ServiceOfferingUpdateRequest updateRequest);

    Set<ServiceOfferingResponse> getAllServiceOfferingsBySalonId(Long salonId, Long categoryId);

    Set<ServiceOfferingResponse> getServiceOfferingsByIds(Set<Long> ids);
}
