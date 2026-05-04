package com.lamnd.repository;

import com.lamnd.entity.Salon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SalonRepo extends JpaRepository<Salon, Long> {
    Optional<Salon> findByOwnerId(Long ownerId);

    @Query(
            "SELECT s FROM Salon s WHERE " +
            "LOWER(s.city) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.address) LIKE LOWER(CONCAT('%', :keyword, '%')) "
    )
    List<Salon> searchSalons(@Param("keyword") String keyword);
}
