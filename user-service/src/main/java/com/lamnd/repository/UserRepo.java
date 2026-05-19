package com.lamnd.repository;

import com.lamnd.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phone);
    boolean existsByUsername(String userName);
    boolean existsByEmailAndIdNot(String email, Long id);
    boolean existsByPhoneNumberAndIdNot(String phone, Long id);
    boolean existsByUsernameAndIdNot(String userName, Long id);
    Optional<User> findByEmail(String email);
}
