package com.lamnd.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lamnd.common.ApiResponse;
import com.lamnd.common.BaseController;
import com.lamnd.dto.*;
import com.lamnd.dto.request.BookingCreateRequest;
import com.lamnd.dto.response.BookingResponse;
import com.lamnd.dto.response.BookingSlotResponse;
import com.lamnd.enums.BookingStatus;
import com.lamnd.enums.PaymentMethod;
import com.lamnd.service.BookingService;
import com.lamnd.service.client.PaymentFeignClient;
import com.lamnd.service.client.SalonFeignClient;
import com.lamnd.service.client.ServiceOfferingFeignClient;
import com.lamnd.service.client.UserFeignClient;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookings")
public class BookingController extends BaseController {

    private final BookingService bookingService;
    private final UserFeignClient userFeignClient;
    private final SalonFeignClient salonFeignClient;
    private final PaymentFeignClient paymentFeignClient;
    private final ServiceOfferingFeignClient serviceOfferingFeignClient;
    private final ObjectMapper objectMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<?> createBooking(
            @RequestParam Long salonId,
            @RequestParam PaymentMethod paymentMethod,
            @RequestBody @Valid BookingCreateRequest createRequest,
            @RequestHeader("Authorization") String token
    ){
        UserDTO user = objectMapper
                .convertValue(Objects.requireNonNull(userFeignClient.getMyInfo(token).getBody()).data(), UserDTO.class);

        SalonDTO salon = objectMapper
                .convertValue(Objects.requireNonNull(salonFeignClient.getSalonById(salonId).getBody()).data(), SalonDTO.class);

        Set<ServiceDTO> services = objectMapper.convertValue(
                Objects.requireNonNull(serviceOfferingFeignClient.getServiceOfferingsByIds(createRequest.serviceIds(), salon.id()).getBody()).data(),
                objectMapper.getTypeFactory().constructCollectionType(Set.class, ServiceDTO.class)
        );

        if (services.isEmpty()) {
            throw new IllegalArgumentException("Services not found");
        }else if (services.size() != createRequest.serviceIds().size()) {
            throw new IllegalArgumentException("Some services not found");
        }

        BookingResponse savedBooking = bookingService.createBooking(
                createRequest,
                user,
                salon,
                services
        );

        PaymentLinkResponse paymentLinkResponse = objectMapper.convertValue(
                paymentFeignClient.createPaymentLink(savedBooking, paymentMethod, token).data(),
                PaymentLinkResponse.class);

        return createSuccessResponse(paymentLinkResponse);
    }

    @GetMapping("/customer")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> getBookingByCustomer(
            @RequestHeader("Authorization") String token
    ){
        UserDTO user = objectMapper
                .convertValue(Objects.requireNonNull(userFeignClient.getMyInfo(token).getBody()).data(), UserDTO.class);

        List<BookingResponse> bookings = bookingService.getBookingsByCustomerId(user.id());

        return createSuccessResponse(bookings);
    }

    @GetMapping("/salon")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> getBookingBySalon(
            @RequestHeader("Authorization") String token
    ){
        SalonDTO salon = objectMapper
                .convertValue(Objects.requireNonNull(salonFeignClient.getSalonByOwnerId(token).getBody()).data(), SalonDTO.class);

        List<BookingResponse> bookings = bookingService.getBookingsBySalonId(salon.id());

        return createSuccessResponse(bookings);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> getBookingById(@PathVariable("id") Long id){
        BookingResponse booking = bookingService.getBookingById(id);

        return createSuccessResponse(booking);
    }

    @GetMapping("/slot/salon/{salonId}/date/{date}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> GetBookingSlot(
            @PathVariable("salonId") Long salonId,
            @RequestParam LocalDate date
    ){
        List<BookingSlotResponse> bookingSlots = bookingService.getBookingByDate(date, salonId);

        return createSuccessResponse(bookingSlots);
    }

    @GetMapping("/report")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> getSalonReport(
            @RequestHeader("Authorization") String token
    ){
        SalonDTO salon = objectMapper
                .convertValue(Objects.requireNonNull(salonFeignClient.getSalonByOwnerId(token).getBody()).data(), SalonDTO.class);

        SalonReport report = bookingService.getSalonReport(salon.id());

        return createSuccessResponse(report);
    }

    @PutMapping("/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> updateBookingStatus(
            @PathVariable("id") Long id,
            @RequestParam BookingStatus status
    ){
        BookingResponse booking = bookingService.updateBookingStatus(id, status);

        return createSuccessResponse(booking);
    }
}
