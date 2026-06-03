package com.lamnd.mapper;

import com.lamnd.dto.SalonDTO;
import com.lamnd.dto.ServiceDTO;
import com.lamnd.dto.UserDTO;
import com.lamnd.dto.response.BookingResponse;
import com.lamnd.entity.Booking;

import java.util.Set;

public class BookingMapper {

    public static BookingResponse toDTO(Booking booking,
                                        Set<ServiceDTO> services,
                                        SalonDTO salon,
                                        UserDTO customer) {
        return BookingResponse.builder()
                .id(booking.getId())
                .customerId(booking.getCustomerId())
                .salonId(booking.getSalonId())
                .serviceIds(booking.getServiceIds())
                .startTime(booking.getStartTime())
                .endTime(booking.getEndTime())
                .status(booking.getStatus())
                .totalPrice(booking.getTotalPrice())
                .services(services)
                .salon(salon)
                .customer(customer)
                .build();
    }
}
