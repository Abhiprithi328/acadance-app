package com.acadance.app.dto;

public class AuthResponse {

    private String token;
    private Long userId;
    private String name;
    private Integer attendanceRequirement;

    public AuthResponse(String token, Long userId, String name, Integer attendanceRequirement) {
        this.token = token;
        this.userId = userId;
        this.name = name;
        this.attendanceRequirement = attendanceRequirement;
    }

    public String getToken() { return token; }
    public Long getUserId() { return userId; }
    public String getName() { return name; }
    public Integer getAttendanceRequirement() { return attendanceRequirement; }
}
