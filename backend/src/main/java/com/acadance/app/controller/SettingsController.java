package com.acadance.app.controller;

import com.acadance.app.dto.MiscDto;
import com.acadance.app.entity.User;
import com.acadance.app.repository.UserRepository;
import com.acadance.app.security.CurrentUser;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/settings")
public class SettingsController {

    private final UserRepository userRepository;

    public SettingsController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/me")
    public Map<String, Object> me() {
        User user = userRepository.findById(CurrentUser.id()).orElseThrow();
        return Map.of(
                "id", user.getId(),
                "name", user.getName(),
                "attendanceRequirement", user.getAttendanceRequirement()
        );
    }

    @PutMapping
    public Map<String, Object> updateSettings(@RequestBody MiscDto.UpdateSettingsRequest request) {
        User user = userRepository.findById(CurrentUser.id()).orElseThrow();
        if (request.getAttendanceRequirement() != null) {
            user.setAttendanceRequirement(request.getAttendanceRequirement());
        }
        userRepository.save(user);
        return me();
    }
}
