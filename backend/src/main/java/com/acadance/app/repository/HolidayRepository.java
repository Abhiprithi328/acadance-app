package com.acadance.app.repository;

import com.acadance.app.entity.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {
    List<Holiday> findByUserId(Long userId);
    Optional<Holiday> findByUserIdAndDate(Long userId, LocalDate date);
    boolean existsByUserIdAndDate(Long userId, LocalDate date);
}
