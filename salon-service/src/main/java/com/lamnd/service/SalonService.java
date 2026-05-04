package com.lamnd.service;

import com.lamnd.dto.UserDTO;
import com.lamnd.dto.request.SalonCreateRequest;
import com.lamnd.dto.request.SalonUpdateRequest;
import com.lamnd.dto.response.SalonResponse;

import java.util.List;

public interface SalonService {
    SalonResponse createSalon(SalonCreateRequest request, UserDTO userDTO);
    List<SalonResponse> getAllSalons();
    SalonResponse getSalonById(Long salonId);
    SalonResponse getSalonByOwnerId(Long ownerId);
    List<SalonResponse> searchSalonsByCity(String city);
    SalonResponse updateSalon(Long salonId, SalonUpdateRequest request, UserDTO userDTO);
    void deleteSalon(Long salonId, UserDTO userDTO);
}
