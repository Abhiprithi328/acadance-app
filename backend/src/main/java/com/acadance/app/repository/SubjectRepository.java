package com.acadance.app.repository;

import com.acadance.app.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findByUserId(Long userId);
}
