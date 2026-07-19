package com.acadance.app.service;

import com.acadance.app.dto.MiscDto;
import com.acadance.app.entity.Holiday;
import com.acadance.app.entity.User;
import com.acadance.app.repository.HolidayRepository;
import com.acadance.app.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HolidayService {

    private final HolidayRepository holidayRepository;
    private final UserRepository userRepository;

    public HolidayService(HolidayRepository holidayRepository, UserRepository userRepository) {
        this.holidayRepository = holidayRepository;
        this.userRepository = userRepository;
    }

    public List<MiscDto.HolidayResponse> getAll(Long userId) {
        return holidayRepository.findByUserId(userId).stream()
                .map(h -> new MiscDto.HolidayResponse(h.getId(), h.getDate(), h.getReason()))
                .toList();
    }

    public List<MiscDto.HolidayResponse> markHolidays(Long userId, MiscDto.HolidayRequest request) {
        User user = userRepository.findById(userId).orElseThrow();
        request.getDates().forEach(date -> {
            if (!holidayRepository.existsByUserIdAndDate(userId, date)) {
                holidayRepository.save(new Holiday(date, request.getReason(), user));
            }
        });
        return getAll(userId);
    }

    public void removeHoliday(Long userId, Long holidayId) {
        Holiday holiday = holidayRepository.findById(holidayId)
                .orElseThrow(() -> new IllegalArgumentException("Holiday not found"));
        if (!holiday.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Not your holiday entry");
        }
        holidayRepository.delete(holiday);
    }

    public boolean isHoliday(Long userId, java.time.LocalDate date) {
        return holidayRepository.existsByUserIdAndDate(userId, date);
    }
}
