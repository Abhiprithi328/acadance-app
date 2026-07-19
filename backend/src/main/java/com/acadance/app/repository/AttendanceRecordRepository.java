package com.acadance.app.repository;

import com.acadance.app.entity.AttendanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long> {
    List<AttendanceRecord> findByUserId(Long userId);
    List<AttendanceRecord> findByUserIdAndSubjectId(Long userId, Long subjectId);
    List<AttendanceRecord> findByUserIdAndDate(Long userId, LocalDate date);
    Optional<AttendanceRecord> findByUserIdAndSubjectIdAndDate(Long userId, Long subjectId, LocalDate date);
}
