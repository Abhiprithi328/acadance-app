package com.acadance.app.dto;

import java.time.DayOfWeek;
import java.util.List;

public class TimetableDto {

    // One row: e.g. Monday, Period 1, Subject "Java"
    public static class Entry {
        private DayOfWeek dayOfWeek;
        private Integer periodNumber;
        private Long subjectId;
        private String subjectName; // only populated in responses, ignored in requests

        public DayOfWeek getDayOfWeek() { return dayOfWeek; }
        public void setDayOfWeek(DayOfWeek dayOfWeek) { this.dayOfWeek = dayOfWeek; }
        public Integer getPeriodNumber() { return periodNumber; }
        public void setPeriodNumber(Integer periodNumber) { this.periodNumber = periodNumber; }
        public Long getSubjectId() { return subjectId; }
        public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }
        public String getSubjectName() { return subjectName; }
        public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
    }

    // Body sent when saving the whole week at once
    public static class SaveRequest {
        private List<Entry> entries;
        public List<Entry> getEntries() { return entries; }
        public void setEntries(List<Entry> entries) { this.entries = entries; }
    }
}
