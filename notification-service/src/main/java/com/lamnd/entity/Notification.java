package com.lamnd.entity;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Notification extends Auditable{
    private String type;
    private String description;
    private Boolean isRead = false;
    private Long userId;
    private Long bookingId;
    private Long salonId;
}
