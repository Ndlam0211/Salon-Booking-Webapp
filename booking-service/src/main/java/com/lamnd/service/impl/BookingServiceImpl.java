package com.lamnd.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lamnd.dto.SalonDTO;
import com.lamnd.dto.SalonReport;
import com.lamnd.dto.ServiceDTO;
import com.lamnd.dto.UserDTO;
import com.lamnd.dto.request.BookingCreateRequest;
import com.lamnd.dto.response.BookingResponse;
import com.lamnd.dto.response.BookingSlotResponse;
import com.lamnd.entity.Booking;
import com.lamnd.entity.Payment;
import com.lamnd.enums.BookingStatus;
import com.lamnd.mapper.BookingMapper;
import com.lamnd.repository.BookingRepo;
import com.lamnd.service.BookingService;
import com.lamnd.service.client.SalonFeignClient;
import com.lamnd.service.client.ServiceOfferingFeignClient;
import com.lamnd.service.client.UserFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepo bookingRepository;
    private final UserFeignClient userFeignClient;
    private final SalonFeignClient salonFeignClient;
    private final ServiceOfferingFeignClient serviceOfferingFeignClient;
    private final ObjectMapper objectMapper;

    @Override
    public BookingResponse createBooking(BookingCreateRequest createRequest,
                                         UserDTO user,
                                         SalonDTO salon,
                                         Set<ServiceDTO> services)
    {
        int totalDuration = services.stream()
                .mapToInt(ServiceDTO::duration).sum();

        LocalDateTime bookingStartTime = createRequest.startTime();
        LocalDateTime bookingEndTime = bookingStartTime.plusMinutes(totalDuration);

        Boolean isAvailable = isTimeSlotAvailable(salon, bookingStartTime, bookingEndTime);

        if (!isAvailable) {
            throw new IllegalArgumentException("Slot is not available, please choose another time");
        }

        Double totalPrice = services.stream()
                .mapToDouble(ServiceDTO::price).sum();

        Set<Long> serviceIds = services.stream()
                .map(ServiceDTO::id)
                .collect(Collectors.toSet());

        Booking newBooking = new Booking();
        newBooking.setCustomerId(user.id());
        newBooking.setSalonId(salon.id());
        newBooking.setStartTime(bookingStartTime);
        newBooking.setEndTime(bookingEndTime);
        newBooking.setServiceIds(serviceIds);
        newBooking.setTotalPrice(totalPrice);
        newBooking.setStatus(BookingStatus.PENDING);

        return BookingMapper.toDTO(bookingRepository.save(newBooking), services, salon, user);
    }

    @Override
    public List<BookingResponse> getBookingsByCustomerId(Long customerId) {
        List<Booking> bookings = bookingRepository.findByCustomerId(customerId);

        return getListResponse(bookings);
    }

    @Override
    public List<BookingResponse> getBookingsBySalonId(Long salonId) {
        List<Booking> bookings = bookingRepository.findBySalonId(salonId);

        return getListResponse(bookings);
    }

    @Override
    public BookingResponse getBookingById(Long bookingId) {
        Booking booking = findBookingById(bookingId);

        return getResponse(booking);
    }

    @Override
    public BookingResponse updateBookingStatus(Long bookingId, BookingStatus status) {
        Booking booking = findBookingById(bookingId);
        booking.setStatus(status);

        Booking updateBooking = bookingRepository.save(booking);

        return getResponse(updateBooking);
    }

    private BookingResponse getResponse(Booking booking) {
        UserDTO customer = objectMapper
                .convertValue(Objects.requireNonNull(userFeignClient.getUserById(booking.getCustomerId()).getBody()).data(), UserDTO.class);

        SalonDTO salon = objectMapper
                .convertValue(Objects.requireNonNull(salonFeignClient.getSalonById(booking.getSalonId()).getBody()).data(), SalonDTO.class);

        Set<ServiceDTO> services = objectMapper.convertValue(
                Objects.requireNonNull(serviceOfferingFeignClient.getServiceOfferingsByIds(booking.getServiceIds(), salon.id()).getBody()).data(),
                objectMapper.getTypeFactory().constructCollectionType(Set.class, ServiceDTO.class)
        );

        return BookingMapper.toDTO(booking, services, salon, customer);
    }

    private List<BookingResponse> getListResponse(List<Booking> bookings) {
        return bookings.stream().map(this::getResponse).toList();
    }

    @Override
    public List<BookingSlotResponse> getBookingByDate(LocalDate date, Long salonId) {
        List<BookingResponse> bookings = getBookingsBySalonId(salonId);

        if(date == null) {
            return bookings.stream()
                    .map(booking -> BookingSlotResponse.builder()
                            .startTime(booking.startTime())
                            .endTime(booking.endTime())
                            .build()).toList();
        }

        return bookings.stream()
                .filter(booking -> isSameDate(booking.startTime(), date) ||
                        isSameDate(booking.endTime(), date))
                .map(booking -> BookingSlotResponse.builder()
                        .startTime(booking.startTime())
                        .endTime(booking.endTime())
                        .build())
                .toList();
    }


    @Override
    public SalonReport getSalonReport(Long salonId) {
        List<BookingResponse> bookings = getBookingsBySalonId(salonId);

        Double totalEarnings = bookings.stream()
                .filter(booking -> booking.status().equals(BookingStatus.COMPLETED))
                .mapToDouble(BookingResponse::totalPrice)
                .sum();

        Integer totalBookings = bookings.size();

        List<BookingResponse> cancelledBookings = bookings.stream()
                .filter(booking -> booking.status().equals(BookingStatus.CANCELLED))
                .toList();

        Double totalRefund = cancelledBookings.stream()
                .mapToDouble(BookingResponse::totalPrice)
                .sum();


        return SalonReport.builder()
                .salonId(salonId)
                .totalBookings(totalBookings)
                .totalEarnings(totalEarnings)
                .cancelledBookings(cancelledBookings.size())
                .totalRefund(totalRefund)
                .build();
    }

    @Override
    public void updateBookingStatus(Payment payment) {
        Booking existingBooking = findBookingById(payment.getBookingId());

        existingBooking.setStatus(BookingStatus.CONFIRMED);

        bookingRepository.save(existingBooking);
    }

    private boolean isSameDate(LocalDateTime localDateTime, LocalDate date) {
        return localDateTime.toLocalDate().isEqual(date);
    }

    private Boolean isTimeSlotAvailable(SalonDTO salon, LocalDateTime bookingStartTime, LocalDateTime bookingEndTime) {
        // Kiểm tra xem booking co nam trong thoi gian hoat dong cua salon hay khong
        LocalDateTime salonOpenTime = salon.openingTime().atDate(bookingStartTime.toLocalDate());
        LocalDateTime salonCloseTime = salon.closingTime().atDate(bookingEndTime.toLocalDate());

        if (bookingStartTime.isBefore(salonOpenTime) || bookingEndTime.isAfter(salonCloseTime)) {
            throw new IllegalArgumentException("Booking time must be within salon operating hours");
        }

        List<BookingResponse> existingBookings = getBookingsBySalonId(salon.id());

        for (BookingResponse existingBooking : existingBookings) {
            LocalDateTime existingStartTime = existingBooking.startTime();
            LocalDateTime existingEndTime = existingBooking.endTime();

            if (bookingStartTime.isBefore(existingEndTime) && bookingEndTime.isAfter(existingStartTime)) {
                return false; // Slot is not available
            }

            if (bookingStartTime.equals(existingStartTime) || bookingEndTime.equals(existingEndTime)) {
                return false; // Slot is not available
            }
        }

        return true;
    }

    private Booking findBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + bookingId));
    }
}
