package com.acadance.app.service;

import com.acadance.app.dto.TimetableDto;
import com.acadance.app.entity.Subject;
import com.acadance.app.entity.User;
import com.acadance.app.entity.WeeklyTimetable;
import com.acadance.app.repository.SubjectRepository;
import com.acadance.app.repository.UserRepository;
import com.acadance.app.repository.WeeklyTimetableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TimetableService {

    private final WeeklyTimetableRepository timetableRepository;
    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;

    public TimetableService(WeeklyTimetableRepository timetableRepository,
                             SubjectRepository subjectRepository,
                             UserRepository userRepository) {
        this.timetableRepository = timetableRepository;
        this.subjectRepository = subjectRepository;
        this.userRepository = userRepository;
    }

    public List<TimetableDto.Entry> getAll(Long userId) {
        return timetableRepository.findByUserIdOrderByDayOfWeekAscPeriodNumberAsc(userId).stream()
                .map(this::toDto)
                .toList();
    }

    // Replaces the ENTIRE weekly timetable in one go - matches "create once, edit later" from the spec
    @Transactional
    public List<TimetableDto.Entry> saveAll(Long userId, List<TimetableDto.Entry> entries) {
        User user = userRepository.findById(userId).orElseThrow();
        timetableRepository.deleteByUserId(userId);

        List<WeeklyTimetable> toSave = entries.stream().map(e -> {
            Subject subject = subjectRepository.findById(e.getSubjectId())
                    .orElseThrow(() -> new IllegalArgumentException("Subject not found: " + e.getSubjectId()));
            return new WeeklyTimetable(e.getDayOfWeek(), e.getPeriodNumber(), subject, user);
        }).toList();

        timetableRepository.saveAll(toSave);
        return getAll(userId);
    }

    private TimetableDto.Entry toDto(WeeklyTimetable t) {
        TimetableDto.Entry dto = new TimetableDto.Entry();
        dto.setDayOfWeek(t.getDayOfWeek());
        dto.setPeriodNumber(t.getPeriodNumber());
        dto.setSubjectId(t.getSubject().getId());
        dto.setSubjectName(t.getSubject().getName());
        return dto;
    }
}
