package com.lamnd.enitity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Data
@Table(name = "categories")
public class Category {
    @Column(nullable = false)
    private String name;
    private String image;
    @Column(nullable = false)
    private Long salonId;
}
