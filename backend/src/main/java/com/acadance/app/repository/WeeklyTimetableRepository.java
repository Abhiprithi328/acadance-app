package com.acadance.app.repository;

import com.acadance.app.entity.WeeklyTimetable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;

public interface WeeklyTimetableRepository extends JpaRepository<WeeklyTimetable, Long> {
    List<WeeklyTimetable> findByUserIdOrderByDayOfWeekAscPeriodNumberAsc(Long userId);
    List<WeeklyTimetable> findByUserIdAndDayOfWeekOrderByPeriodNumberAsc(Long userId, DayOfWeek dayOfWeek);
    void deleteByUserId(Long userId);
}
