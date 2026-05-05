package com.lamnd.enitity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "categories")
public class Category extends Auditable{
    @Column(nullable = false)
    private String name;
    private String image;
    @Column(nullable = false)
    private Long salonId;
}
