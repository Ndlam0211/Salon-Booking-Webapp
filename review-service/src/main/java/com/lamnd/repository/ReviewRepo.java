package com.lamnd.repository;

import com.lamnd.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepo extends JpaRepository<Review, Long> {

    List<Review> findBySalonId(Long salonId);
    List<Review> findByUserId(Long userId);
}
