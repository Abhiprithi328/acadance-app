package com.acadance.app.service;

import com.acadance.app.dto.SubjectDto;
import com.acadance.app.entity.Subject;
import com.acadance.app.entity.User;
import com.acadance.app.repository.SubjectRepository;
import com.acadance.app.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;

    public SubjectService(SubjectRepository subjectRepository, UserRepository userRepository) {
        this.subjectRepository = subjectRepository;
        this.userRepository = userRepository;
    }

    public List<SubjectDto.Response> getAll(Long userId) {
        return subjectRepository.findByUserId(userId).stream()
                .map(s -> new SubjectDto.Response(s.getId(), s.getName()))
                .toList();
    }

    public SubjectDto.Response create(Long userId, String name) {
        User user = userRepository.findById(userId).orElseThrow();
        Subject subject = subjectRepository.save(new Subject(name, user));
        return new SubjectDto.Response(subject.getId(), subject.getName());
    }

    public SubjectDto.Response update(Long userId, Long subjectId, String name) {
        Subject subject = subjectRepository.findById(subjectId).orElseThrow(
                () -> new IllegalArgumentException("Subject not found"));
        if (!subject.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Not your subject");
        }
        subject.setName(name);
        subjectRepository.save(subject);
        return new SubjectDto.Response(subject.getId(), subject.getName());
    }

    public void delete(Long userId, Long subjectId) {
        Subject subject = subjectRepository.findById(subjectId).orElseThrow(
                () -> new IllegalArgumentException("Subject not found"));
        if (!subject.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Not your subject");
        }
        subjectRepository.delete(subject);
    }
}
