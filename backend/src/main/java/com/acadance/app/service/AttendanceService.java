package com.acadance.app.service;

import com.acadance.app.dto.AttendanceDto;
import com.acadance.app.entity.*;
import com.acadance.app.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    private final AttendanceRecordRepository attendanceRepository;
    private final WeeklyTimetableRepository timetableRepository;
    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;
    private final HolidayService holidayService;

    public AttendanceService(AttendanceRecordRepository attendanceRepository,
                              WeeklyTimetableRepository timetableRepository,
                              SubjectRepository subjectRepository,
                              UserRepository userRepository,
                              HolidayService holidayService) {
        this.attendanceRepository = attendanceRepository;
        this.timetableRepository = timetableRepository;
        this.subjectRepository = subjectRepository;
        this.userRepository = userRepository;
        this.holidayService = holidayService;
    }

    // Returns today's scheduled classes, with PRESENT/ABSENT/null (not yet marked) for each
    public List<AttendanceDto.TodayClass> getClassesForDate(Long userId, LocalDate date) {
        if (holidayService.isHoliday(userId, date)) {
            return List.of(); // no classes shown on a holiday
        }

        DayOfWeek day = date.getDayOfWeek();
        List<WeeklyTimetable> scheduled = timetableRepository
                .findByUserIdAndDayOfWeekOrderByPeriodNumberAsc(userId, day);

        Map<Long, AttendanceRecord> marksBySubject = attendanceRepository
                .findByUserIdAndDate(userId, date).stream()
                .collect(Collectors.toMap(r -> r.getSubject().getId(), r -> r));

        return scheduled.stream().map(t -> {
            AttendanceRecord mark = marksBySubject.get(t.getSubject().getId());
            String status = mark == null ? null : mark.getStatus().name();
            return new AttendanceDto.TodayClass(
                    t.getSubject().getId(), t.getSubject().getName(), t.getPeriodNumber(), status);
        }).toList();
    }

    // Saves Present/Absent for each subject on a given date (rejects holidays)
    @Transactional
    public void markAttendance(Long userId, AttendanceDto.MarkRequest request) {
        if (holidayService.isHoliday(userId, request.getDate())) {
            throw new IllegalArgumentException("Cannot mark attendance on a holiday");
        }

        User user = userRepository.findById(userId).orElseThrow();

        for (AttendanceDto.MarkEntry entry : request.getEntries()) {
            Subject subject = subjectRepository.findById(entry.getSubjectId())
                    .orElseThrow(() -> new IllegalArgumentException("Subject not found"));

            AttendanceStatus status = AttendanceStatus.valueOf(entry.getStatus().toUpperCase());

            AttendanceRecord record = attendanceRepository
                    .findByUserIdAndSubjectIdAndDate(userId, subject.getId(), request.getDate())
                    .orElse(new AttendanceRecord(request.getDate(), subject, status, user));

            record.setStatus(status); // update if it already existed (user changed their mind)
            attendanceRepository.save(record);
        }
    }

    // Computes overall % and per-subject % - this is what powers the Dashboard
    public AttendanceDto.StatsResponse getStats(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        int requirement = user.getAttendanceRequirement();

        List<Subject> subjects = subjectRepository.findByUserId(userId);
        List<AttendanceRecord> allRecords = attendanceRepository.findByUserId(userId);

        Map<Long, List<AttendanceRecord>> bySubject = allRecords.stream()
                .collect(Collectors.groupingBy(r -> r.getSubject().getId()));

        List<AttendanceDto.SubjectStat> subjectStats = new ArrayList<>();
        int totalConducted = 0;
        int totalAttended = 0;

        for (Subject subject : subjects) {
            List<AttendanceRecord> records = bySubject.getOrDefault(subject.getId(), List.of());
            int conducted = records.size();
            int attended = (int) records.stream().filter(r -> r.getStatus() == AttendanceStatus.PRESENT).count();
            double pct = conducted == 0 ? 100.0 : round2((attended * 100.0) / conducted);

            subjectStats.add(new AttendanceDto.SubjectStat(
                    subject.getId(), subject.getName(), pct, conducted, attended, riskLevel(pct, requirement)));

            totalConducted += conducted;
            totalAttended += attended;
        }

        double overallPct = totalConducted == 0 ? 100.0 : round2((totalAttended * 100.0) / totalConducted);

        return new AttendanceDto.StatsResponse(
                overallPct, totalConducted, totalAttended, riskLevel(overallPct, requirement), subjectStats);
    }

    private String riskLevel(double percentage, int requirement) {
        if (percentage >= requirement) return "SAFE";
        if (percentage >= requirement - 10) return "WARNING";
        return "CRITICAL";
    }

    private double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
