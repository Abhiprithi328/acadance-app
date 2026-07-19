package com.acadance.app.dto;

import java.time.LocalDate;
import java.util.List;

public class MiscDto {

    // Body to mark one or more dates as holiday
    public static class HolidayRequest {
        private List<LocalDate> dates;
        private String reason;
        public List<LocalDate> getDates() { return dates; }
        public void setDates(List<LocalDate> dates) { this.dates = dates; }
        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
    }

    public static class HolidayResponse {
        private Long id;
        private LocalDate date;
        private String reason;

        public HolidayResponse(Long id, LocalDate date, String reason) {
            this.id = id;
            this.date = date;
            this.reason = reason;
        }

        public Long getId() { return id; }
        public LocalDate getDate() { return date; }
        public String getReason() { return reason; }
    }

    // Body to update attendance requirement in Settings
    public static class UpdateSettingsRequest {
        private Integer attendanceRequirement;
        public Integer getAttendanceRequirement() { return attendanceRequirement; }
        public void setAttendanceRequirement(Integer attendanceRequirement) { this.attendanceRequirement = attendanceRequirement; }
    }
}
