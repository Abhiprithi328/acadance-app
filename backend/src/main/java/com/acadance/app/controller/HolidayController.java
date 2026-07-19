package com.acadance.app.controller;

import com.acadance.app.dto.MiscDto;
import com.acadance.app.security.CurrentUser;
import com.acadance.app.service.HolidayService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/holidays")
public class HolidayController {

    private final HolidayService holidayService;

    public HolidayController(HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    @GetMapping
    public List<MiscDto.HolidayResponse> getAll() {
        return holidayService.getAll(CurrentUser.id());
    }

    @PostMapping
    public List<MiscDto.HolidayResponse> mark(@RequestBody MiscDto.HolidayRequest request) {
        return holidayService.markHolidays(CurrentUser.id(), request);
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable Long id) {
        holidayService.removeHoliday(CurrentUser.id(), id);
    }
}
