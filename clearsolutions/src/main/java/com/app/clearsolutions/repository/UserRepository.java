package com.app.clearsolutions.repository;

import com.app.clearsolutions.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

/**
 * Repository interface for managing User entities in the database.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserById(Long id);
    Page<User> findUsersByIsActiveIsTrue(Pageable pageable);
    Page<User> findUsersByBirthDateBetweenAndIsActiveIsTrue(LocalDate from, LocalDate to, Pageable pageable);
    Page<User> findUsersByBirthDateBeforeAndIsActiveIsTrue(LocalDate to, Pageable pageable);
    Page<User> findUsersByBirthDateAfterAndIsActiveIsTrue(LocalDate from, Pageable pageable);

}
