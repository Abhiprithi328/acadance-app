package com.acadance.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SignupRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50)
    private String name;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
