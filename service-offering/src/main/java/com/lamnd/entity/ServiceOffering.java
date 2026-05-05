package com.lamnd.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "service_offerings")
public class ServiceOffering extends Auditable {
    @Column(nullable = false)
    private String name;
    private String description;
    private Double price;
    private int duration;
    private String image;
    @Column(nullable = false)
    private Long categoryId;
    @Column(nullable = false)
    private Long salonId;
}
