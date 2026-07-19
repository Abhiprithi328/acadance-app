package com.acadance.app.controller;

import com.acadance.app.dto.TimetableDto;
import com.acadance.app.security.CurrentUser;
import com.acadance.app.service.TimetableService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/timetable")
public class TimetableController {

    private final TimetableService timetableService;

    public TimetableController(TimetableService timetableService) {
        this.timetableService = timetableService;
    }

    @GetMapping
    public List<TimetableDto.Entry> getAll() {
        return timetableService.getAll(CurrentUser.id());
    }

    // Saves/replaces the whole weekly timetable in one request
    @PostMapping
    public List<TimetableDto.Entry> saveAll(@RequestBody TimetableDto.SaveRequest request) {
        return timetableService.saveAll(CurrentUser.id(), request.getEntries());
    }
}
