package com.lamnd.entity;

import com.lamnd.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bookings", indexes = {
        @Index(name = "idx_salon_id", columnList = "salonId"),
        @Index(name = "idx_customer_id", columnList = "customerId"),
        @Index(name = "idx_salon_start_end_time", columnList = "salonId,startTime,endTime"),
        @Index(name = "idx_booking_status", columnList = "status")
})
public class Booking extends Auditable{
    private Long salonId;
    private Long customerId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @ElementCollection
    private Set<Long> serviceIds;

    private BookingStatus status = BookingStatus.PENDING;
    private Double totalPrice;

    @Version
    private Long version;
}
