package com.lamnd.repository;

import com.lamnd.entity.Booking;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepo extends JpaRepository<Booking, Long> {
    List<Booking> findByCustomerId(Long customerId);

    List<Booking> findBySalonId(Long salonId);

    /**
     * Tìm các booking trùng lịch với pessimistic lock (WRITE lock)
     * Đảm bảo ngăn chặn 2 transaction cùng modify dữ liệu
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM Booking b WHERE b.salonId = :salonId " +
           "AND b.status != 'CANCELLED' " +
           "AND b.startTime < :endTime AND b.endTime > :startTime " +
           "ORDER BY b.startTime ASC")
    List<Booking> findConflictingBookingsWithLock(
            @Param("salonId") Long salonId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    /**
     * Tìm tất cả booking của salon với pessimistic lock
     * Sử dụng trong trường hợp cần lock toàn bộ salon
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM Booking b WHERE b.salonId = :salonId " +
           "AND b.status != 'CANCELLED' " +
           "ORDER BY b.startTime ASC")
    List<Booking> findAllActiveSalonBookingsWithLock(@Param("salonId") Long salonId);
}
