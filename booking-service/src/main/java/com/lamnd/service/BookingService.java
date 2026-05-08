package com.lamnd.service;

import com.lamnd.dto.SalonDTO;
import com.lamnd.dto.SalonReport;
import com.lamnd.dto.ServiceDTO;
import com.lamnd.dto.UserDTO;
import com.lamnd.dto.request.BookingCreateRequest;
import com.lamnd.dto.response.BookingResponse;
import com.lamnd.dto.response.BookingSlotResponse;
import com.lamnd.enums.BookingStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface BookingService {
    BookingResponse createBooking(BookingCreateRequest createRequest,
                                  UserDTO user,
                                  SalonDTO salon,
                                  Set<ServiceDTO> services);

    List<BookingResponse> getBookingsByCustomerId(Long customerId);
    List<BookingResponse> getBookingsBySalonId(Long salonId);
    BookingResponse getBookingById(Long bookingId);
    BookingResponse updateBookingStatus(Long bookingId, BookingStatus status);
    List<BookingSlotResponse> getBookingByDate(LocalDate date, Long salonId);
    SalonReport getSalonReport(Long salonId);
}
