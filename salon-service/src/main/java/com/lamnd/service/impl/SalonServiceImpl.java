package com.lamnd.service.impl;

import com.lamnd.dto.UserDTO;
import com.lamnd.dto.request.SalonCreateRequest;
import com.lamnd.dto.request.SalonUpdateRequest;
import com.lamnd.dto.response.SalonResponse;
import com.lamnd.entity.Salon;
import com.lamnd.exception.ResourceNotFoundException;
import com.lamnd.mapper.SalonMapper;
import com.lamnd.repository.SalonRepo;
import com.lamnd.service.SalonService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SalonServiceImpl implements SalonService {

    private final SalonRepo salonRepo;
    private final SalonMapper salonMapper;

    @Override
    @CacheEvict(value = "salonsCache", allEntries = true)
    public SalonResponse createSalon(SalonCreateRequest request, UserDTO userDTO) {
        Salon salon = salonMapper.toEntity(request);
        salon.setOwnerId(userDTO.id());

        return salonMapper.toDTO(salonRepo.save(salon));
    }

    @Override
    @Cacheable(value = "salonsCache", key = "'allSalons'")
    public List<SalonResponse> getAllSalons() {
        return salonMapper.toList(salonRepo.findAll());
    }

    @Override
    public SalonResponse getSalonById(Long salonId) {
        Salon existingSalon = findSalonById(salonId);
        return salonMapper.toDTO(existingSalon);
    }

    @Override
    public SalonResponse getSalonByOwnerId(Long ownerId) {
        Salon existingSalon = findSalonByOwnerId(ownerId);

        return salonMapper.toDTO(existingSalon);
    }

    @Override
    public List<SalonResponse> searchSalonsByCity(String city) {
        return salonMapper.toList(salonRepo.searchSalons(city));
    }

    @Override
    @CacheEvict(value = "salonsCache", allEntries = true)
    public SalonResponse updateSalon(Long salonId, SalonUpdateRequest request, UserDTO userDTO) {
        Salon existingSalon = findSalonById(salonId);

        // chỉ cho phép chủ salon mới được update
        if (!existingSalon.getOwnerId().equals(userDTO.id()))
            throw new RuntimeException("You are not the owner of this salon");

        salonMapper.updateEntityFromRequest(request, existingSalon);

        return salonMapper.toDTO(salonRepo.save(existingSalon));
    }

    @Override
    @CacheEvict(value = "salonsCache", allEntries = true)
    public void deleteSalon(Long salonId, UserDTO userDTO) {
        Salon existingSalon = findSalonById(salonId);

        // chỉ cho phép chủ salon mới được delete
        if (!existingSalon.getOwnerId().equals(userDTO.id()))
            throw new RuntimeException("You are not the owner of this salon");

        salonRepo.deleteById(salonId);
    }

    private Salon findSalonById(Long salonId) {
        return salonRepo.findById(salonId)
                .orElseThrow(() -> new ResourceNotFoundException("Salon", "id", salonId));
    }

    private Salon findSalonByOwnerId(Long ownerId) {
        return salonRepo.findByOwnerId(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Salon", "owner id", ownerId));
    }
}
