package com.acadance.app.controller;

import com.acadance.app.dto.SubjectDto;
import com.acadance.app.security.CurrentUser;
import com.acadance.app.service.SubjectService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping
    public List<SubjectDto.Response> getAll() {
        return subjectService.getAll(CurrentUser.id());
    }

    @PostMapping
    public SubjectDto.Response create(@Valid @RequestBody SubjectDto.Request request) {
        return subjectService.create(CurrentUser.id(), request.getName());
    }

    @PutMapping("/{id}")
    public SubjectDto.Response update(@PathVariable Long id, @Valid @RequestBody SubjectDto.Request request) {
        return subjectService.update(CurrentUser.id(), id, request.getName());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        subjectService.delete(CurrentUser.id(), id);
    }
}
