package com.lamnd.controller;

import com.lamnd.common.ApiResponse;
import com.lamnd.common.BaseController;
import com.lamnd.dto.UserDTO;
import com.lamnd.dto.request.SalonCreateRequest;
import com.lamnd.dto.request.SalonUpdateRequest;
import com.lamnd.dto.response.SalonResponse;
import com.lamnd.service.SalonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/salons")
public class SalonController extends BaseController {

    private final SalonService salonService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createSalon(@RequestBody SalonCreateRequest request) {
        UserDTO userDTO = UserDTO.builder()
                .id(1L)
                .build();

        SalonResponse response = salonService.createSalon(request, userDTO);

        return new ResponseEntity<>(createSuccessResponse(response), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllSalons() {
        List<SalonResponse> salons = salonService.getAllSalons();

        return ResponseEntity.ok(createSuccessResponse(salons));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getSalonById(@PathVariable("id") Long id) {
        SalonResponse salon = salonService.getSalonById(id);
        return ResponseEntity.ok(createSuccessResponse(salon));
    }

    @GetMapping("/owner")
    public ResponseEntity<ApiResponse<?>> getSalonByOwnerId(@PathVariable("id") Long id) {
        UserDTO userDTO = UserDTO.builder()
                .id(1L)
                .build();

        SalonResponse salon = salonService.getSalonByOwnerId(userDTO.id());
        return ResponseEntity.ok(createSuccessResponse(salon));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<?>> searchSalons(@RequestParam("city") String city) {
        List<SalonResponse> salons = salonService.searchSalonsByCity(city);
        return ResponseEntity.ok(createSuccessResponse(salons));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateSalon(@PathVariable("id") Long id, @RequestBody SalonUpdateRequest request) {
        UserDTO userDTO = UserDTO.builder()
                .id(1L)
                .build();

        SalonResponse response = salonService.updateSalon(id, request, userDTO);

        return ResponseEntity.ok(createSuccessResponse(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSalon(@PathVariable("id") Long id) {
        UserDTO userDTO = UserDTO.builder()
                .id(1L)
                .build();

        salonService.deleteSalon(id, userDTO);

        return ResponseEntity.noContent().build();
    }
}
