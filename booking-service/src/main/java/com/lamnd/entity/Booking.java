package com.lamnd.entity;

import com.lamnd.enums.BookingStatus;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
@Table(name = "bookings")
public class Booking extends Auditable{
    private Long salonId;
    private Long customerId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @ElementCollection
    private Set<Long> serviceIds;

    private BookingStatus status = BookingStatus.PENDING;
    private int totalPrice;
}
