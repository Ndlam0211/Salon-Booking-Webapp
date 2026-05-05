package com.lamnd.repository;

import com.lamnd.enitity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {
    Set<Category> findAllBySalonId(Long salonId);
}
