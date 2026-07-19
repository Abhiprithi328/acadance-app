package com.acadance.app.dto;

import java.time.LocalDate;
import java.util.List;

public class AttendanceDto {

    // One subject's mark for a given day: "Java" -> "PRESENT"
    public static class MarkEntry {
        private Long subjectId;
        private String status; // "PRESENT" or "ABSENT"
        public Long getSubjectId() { return subjectId; }
        public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    // Body sent when marking attendance for a whole day
    public static class MarkRequest {
        private LocalDate date;
        private List<MarkEntry> entries;
        public LocalDate getDate() { return date; }
        public void setDate(LocalDate date) { this.date = date; }
        public List<MarkEntry> getEntries() { return entries; }
        public void setEntries(List<MarkEntry> entries) { this.entries = entries; }
    }

    // What today's schedule looks like, with attendance status if already marked
    public static class TodayClass {
        private Long subjectId;
        private String subjectName;
        private Integer periodNumber;
        private String status; // "PRESENT", "ABSENT", or null if not marked yet

        public TodayClass(Long subjectId, String subjectName, Integer periodNumber, String status) {
            this.subjectId = subjectId;
            this.subjectName = subjectName;
            this.periodNumber = periodNumber;
            this.status = status;
        }

        public Long getSubjectId() { return subjectId; }
        public String getSubjectName() { return subjectName; }
        public Integer getPeriodNumber() { return periodNumber; }
        public String getStatus() { return status; }
    }

    // Per-subject attendance breakdown
    public static class SubjectStat {
        private Long subjectId;
        private String subjectName;
        private double percentage;
        private int conducted;
        private int attended;
        private String riskLevel; // "SAFE", "WARNING", "CRITICAL"

        public SubjectStat(Long subjectId, String subjectName, double percentage,
                            int conducted, int attended, String riskLevel) {
            this.subjectId = subjectId;
            this.subjectName = subjectName;
            this.percentage = percentage;
            this.conducted = conducted;
            this.attended = attended;
            this.riskLevel = riskLevel;
        }

        public Long getSubjectId() { return subjectId; }
        public String getSubjectName() { return subjectName; }
        public double getPercentage() { return percentage; }
        public int getConducted() { return conducted; }
        public int getAttended() { return attended; }
        public String getRiskLevel() { return riskLevel; }
    }

    // Full dashboard stats payload
    public static class StatsResponse {
        private double overallPercentage;
        private int totalConducted;
        private int totalAttended;
        private String riskLevel;
        private List<SubjectStat> subjectStats;

        public StatsResponse(double overallPercentage, int totalConducted, int totalAttended,
                              String riskLevel, List<SubjectStat> subjectStats) {
            this.overallPercentage = overallPercentage;
            this.totalConducted = totalConducted;
            this.totalAttended = totalAttended;
            this.riskLevel = riskLevel;
            this.subjectStats = subjectStats;
        }

        public double getOverallPercentage() { return overallPercentage; }
        public int getTotalConducted() { return totalConducted; }
        public int getTotalAttended() { return totalAttended; }
        public String getRiskLevel() { return riskLevel; }
        public List<SubjectStat> getSubjectStats() { return subjectStats; }
    }
}
