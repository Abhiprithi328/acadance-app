package com.acadance.app.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "holidays",
       uniqueConstraints = @UniqueConstraint(columnNames = {"date", "user_id"}))
public class Holiday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    private String reason;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Holiday() {}

    public Holiday(LocalDate date, String reason, User user) {
        this.date = date;
        this.reason = reason;
        this.user = user;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
