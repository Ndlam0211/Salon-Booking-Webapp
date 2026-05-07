package com.lamnd.service.impl;

import com.lamnd.dto.SalonDTO;
import com.lamnd.dto.SalonReport;
import com.lamnd.dto.ServiceDTO;
import com.lamnd.dto.UserDTO;
import com.lamnd.dto.request.BookingCreateRequest;
import com.lamnd.dto.response.BookingResponse;
import com.lamnd.enums.BookingStatus;
import com.lamnd.service.BookingService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
public class BookingServiceImpl implements BookingService {
    @Override
    public BookingResponse createBooking(BookingCreateRequest createRequest, UserDTO user, SalonDTO salon, Set<ServiceDTO> services) {
        return null;
    }

    @Override
    public List<BookingResponse> getBookingsByCustomerId(Long customerId) {
        return List.of();
    }

    @Override
    public List<BookingResponse> getBookingsBySalonId(Long salonId) {
        return List.of();
    }

    @Override
    public BookingResponse getBookingById(Long bookingId) {
        return null;
    }

    @Override
    public BookingResponse updateBookingStatus(Long bookingId, BookingStatus status) {
        return null;
    }

    @Override
    public List<BookingResponse> getBookingByDate(LocalDate date, Long salonId) {
        return List.of();
    }

    @Override
    public SalonReport getSalonReport(Long salonId) {
        return null;
    }
}
