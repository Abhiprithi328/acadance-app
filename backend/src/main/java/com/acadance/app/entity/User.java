package com.acadance.app.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String password; // stored as a bcrypt hash, never plain text

    @Column(nullable = false)
    private Integer attendanceRequirement = 75; // default 75%, editable later in Settings

    public User() {}

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Integer getAttendanceRequirement() { return attendanceRequirement; }
    public void setAttendanceRequirement(Integer attendanceRequirement) {
        this.attendanceRequirement = attendanceRequirement;
    }
}
