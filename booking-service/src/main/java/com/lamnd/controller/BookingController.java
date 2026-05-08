package com.lamnd.controller;

import com.lamnd.common.ApiResponse;
import com.lamnd.common.BaseController;
import com.lamnd.dto.SalonDTO;
import com.lamnd.dto.SalonReport;
import com.lamnd.dto.ServiceDTO;
import com.lamnd.dto.UserDTO;
import com.lamnd.dto.request.BookingCreateRequest;
import com.lamnd.dto.response.BookingResponse;
import com.lamnd.dto.response.BookingSlotResponse;
import com.lamnd.enums.BookingStatus;
import com.lamnd.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookings")
public class BookingController extends BaseController {

    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<?> createBooking(
            @RequestParam Long salonId,
            @RequestBody @Valid BookingCreateRequest createRequest
    ){
        UserDTO user = UserDTO.builder()
                .id(1L)
                .build();

        SalonDTO salon = SalonDTO.builder()
                .id(salonId)
                .build();

        Set<ServiceDTO> services = new HashSet<>();

        ServiceDTO serviceDTO = ServiceDTO.builder()
                .id(1L)
                .price(10000.0)
                .duration(15)
                .name("Haircut for men")
                .build();

        services.add(serviceDTO);

        BookingResponse savedBooking = bookingService.createBooking(
                createRequest,
                user,
                salon,
                services
        );

        return createSuccessResponse(savedBooking);
    }

    @GetMapping("/customer")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> getBookingByCustomer(){
        UserDTO user = UserDTO.builder()
                .id(1L)
                .build();

        List<BookingResponse> bookings = bookingService.getBookingsByCustomerId(user.id());

        return createSuccessResponse(bookings);
    }

    @GetMapping("/salon")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> getBookingBySalon(){
        List<BookingResponse> bookings = bookingService.getBookingsBySalonId(1L);

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
    public ApiResponse<?> getSalonReport(){
        SalonReport report = bookingService.getSalonReport(1L);

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
