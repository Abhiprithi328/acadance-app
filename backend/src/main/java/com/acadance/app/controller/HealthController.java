package com.acadance.app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/attendance")
public class HealthController {

    @GetMapping("/health")
    public String health() {
        return "Acadance backend is running";
    }
}
