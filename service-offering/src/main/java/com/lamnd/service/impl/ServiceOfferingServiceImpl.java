package com.lamnd.service.impl;

import com.lamnd.dto.CategoryDTO;
import com.lamnd.dto.SalonDTO;
import com.lamnd.dto.request.ServiceOfferingCreateRequest;
import com.lamnd.dto.request.ServiceOfferingUpdateRequest;
import com.lamnd.dto.response.ServiceOfferingResponse;
import com.lamnd.entity.ServiceOffering;
import com.lamnd.mapper.ServiceOfferingMapper;
import com.lamnd.repository.ServiceOfferingRepo;
import com.lamnd.service.ServiceOfferingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceOfferingServiceImpl implements ServiceOfferingService {

    private final ServiceOfferingRepo serviceOfferingRepo;
    private final ServiceOfferingMapper serviceOfferingMapper;

    @Override
    public ServiceOfferingResponse createServiceOffering(ServiceOfferingCreateRequest createRequest, SalonDTO salon, CategoryDTO category) {
        ServiceOffering serviceOffering = serviceOfferingMapper.toEntity(createRequest);
        serviceOffering.setSalonId(salon.id());
        serviceOffering.setCategoryId(category.id());

        return serviceOfferingMapper.toDTO(serviceOfferingRepo.save(serviceOffering));
    }

    @Override
    public ServiceOfferingResponse updateServiceOffering(Long id, ServiceOfferingUpdateRequest updateRequest) {
        ServiceOffering existingServiceOffering = findServiceOfferingById(id);

        serviceOfferingMapper.updateEntityFromRequest(updateRequest, existingServiceOffering);

        return serviceOfferingMapper.toDTO(serviceOfferingRepo.save(existingServiceOffering));
    }

    @Override
    public Set<ServiceOfferingResponse> getAllServiceOfferingsBySalonId(Long salonId, Long categoryId) {
        Set<ServiceOffering> serviceOfferings = serviceOfferingRepo.findBySalonId(salonId);

        if (categoryId != null) {
            serviceOfferings = serviceOfferings.stream()
                    .filter(serviceOffering -> serviceOffering.getCategoryId().equals(categoryId))
                    .collect(Collectors.toSet());
        }

        return serviceOfferingMapper.toSet(serviceOfferings);
    }

    @Override
    public Set<ServiceOfferingResponse> getServiceOfferingsByIds(Set<Long> ids, Long salonId) {
        Set<ServiceOffering> serviceOfferings = serviceOfferingRepo.findBySalonId(salonId).stream()
                .filter(serviceOffering -> ids.contains(serviceOffering.getId()))
                .collect(Collectors.toSet());

        return serviceOfferingMapper.toSet(serviceOfferings);
    }

    @Override
    public ServiceOfferingResponse getServiceOfferingById(Long id) {
        ServiceOffering serviceOffering = findServiceOfferingById(id);
        return serviceOfferingMapper.toDTO(serviceOffering);
    }

    private ServiceOffering findServiceOfferingById(Long id) {
        return serviceOfferingRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Service offering not found with id: " + id));
    }
}
