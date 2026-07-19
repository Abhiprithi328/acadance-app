package com.acadance.app.entity;

import jakarta.persistence.*;
import java.time.DayOfWeek;

@Entity
@Table(name = "weekly_timetable")
public class WeeklyTimetable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(nullable = false)
    private Integer periodNumber;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public WeeklyTimetable() {}

    public WeeklyTimetable(DayOfWeek dayOfWeek, Integer periodNumber, Subject subject, User user) {
        this.dayOfWeek = dayOfWeek;
        this.periodNumber = periodNumber;
        this.subject = subject;
        this.user = user;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public DayOfWeek getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(DayOfWeek dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    public Integer getPeriodNumber() { return periodNumber; }
    public void setPeriodNumber(Integer periodNumber) { this.periodNumber = periodNumber; }
    public Subject getSubject() { return subject; }
    public void setSubject(Subject subject) { this.subject = subject; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
