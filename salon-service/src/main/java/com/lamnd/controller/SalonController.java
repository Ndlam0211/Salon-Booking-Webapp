package com.lamnd.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lamnd.common.ApiResponse;
import com.lamnd.common.BaseController;
import com.lamnd.dto.UserDTO;
import com.lamnd.dto.request.SalonCreateRequest;
import com.lamnd.dto.request.SalonUpdateRequest;
import com.lamnd.dto.response.SalonResponse;
import com.lamnd.service.SalonService;
import com.lamnd.service.client.UserFeignClient;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/salons")
public class SalonController extends BaseController {

    private final SalonService salonService;
    private final UserFeignClient userFeignClient;
    private final ObjectMapper objectMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createSalon(
            @RequestBody @Valid SalonCreateRequest request,
            @RequestHeader("Authorization") String token) {

        UserDTO userDTO = objectMapper
                .convertValue(Objects.requireNonNull(userFeignClient.getMyInfo(token).getBody()).data(), UserDTO.class);

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
    public ResponseEntity<ApiResponse<?>> getSalonByOwnerId(@RequestHeader("Authorization") String token) {
        UserDTO userDTO = objectMapper
                .convertValue(Objects.requireNonNull(userFeignClient.getMyInfo(token).getBody()).data(), UserDTO.class);

        SalonResponse salon = salonService.getSalonByOwnerId(userDTO.id());
        return ResponseEntity.ok(createSuccessResponse(salon));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<?>> searchSalons(@RequestParam("city") String city) {
        List<SalonResponse> salons = salonService.searchSalonsByCity(city);
        return ResponseEntity.ok(createSuccessResponse(salons));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateSalon(
            @PathVariable("id") Long id,
            @RequestBody @Valid SalonUpdateRequest request,
            @RequestHeader("Authorization") String token) {
        UserDTO userDTO = objectMapper
                .convertValue(Objects.requireNonNull(userFeignClient.getMyInfo(token).getBody()).data(), UserDTO.class);

        SalonResponse response = salonService.updateSalon(id, request, userDTO);

        return ResponseEntity.ok(createSuccessResponse(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSalon(
            @PathVariable("id") Long id,
            @RequestHeader("Authorization") String token) {
        UserDTO userDTO = objectMapper
                .convertValue(Objects.requireNonNull(userFeignClient.getMyInfo(token).getBody()).data(), UserDTO.class);

        salonService.deleteSalon(id, userDTO);

        return ResponseEntity.noContent().build();
    }
}
