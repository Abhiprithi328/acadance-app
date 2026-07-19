package com.acadance.app.controller;

import com.acadance.app.dto.AttendanceDto;
import com.acadance.app.security.CurrentUser;
import com.acadance.app.service.AttendanceService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceRecordController {

    private final AttendanceService attendanceService;

    public AttendanceRecordController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    // Today's classes with current status, e.g. GET /api/attendance/classes?date=2026-07-20
    @GetMapping("/classes")
    public List<AttendanceDto.TodayClass> getClasses(
            @RequestParam(required = false) String date) {
        LocalDate targetDate = date != null ? LocalDate.parse(date) : LocalDate.now();
        return attendanceService.getClassesForDate(CurrentUser.id(), targetDate);
    }

    @PostMapping("/mark")
    public void markAttendance(@RequestBody AttendanceDto.MarkRequest request) {
        attendanceService.markAttendance(CurrentUser.id(), request);
    }

    @GetMapping("/stats")
    public AttendanceDto.StatsResponse getStats() {
        return attendanceService.getStats(CurrentUser.id());
    }
}
