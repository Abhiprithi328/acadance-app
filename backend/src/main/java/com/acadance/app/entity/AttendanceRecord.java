package com.acadance.app.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "attendance_records",
       uniqueConstraints = @UniqueConstraint(columnNames = {"date", "subject_id", "user_id"}))
public class AttendanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public AttendanceRecord() {}

    public AttendanceRecord(LocalDate date, Subject subject, AttendanceStatus status, User user) {
        this.date = date;
        this.subject = subject;
        this.status = status;
        this.user = user;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public Subject getSubject() { return subject; }
    public void setSubject(Subject subject) { this.subject = subject; }
    public AttendanceStatus getStatus() { return status; }
    public void setStatus(AttendanceStatus status) { this.status = status; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
