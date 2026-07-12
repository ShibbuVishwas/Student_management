package com.example.student.controller;

import com.example.student.dto.StudentDto;
import com.example.student.service.SessionService;
import com.example.student.service.StudentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentRestController {

    private final StudentService studentService;
    private final SessionService sessionService;

    @GetMapping
    public List<StudentDto> getAllStudents(HttpSession session) {
        String branch = sessionService.getProfessorBranch(session);
        return studentService.getStudentsByBranch(branch);
    }

    @GetMapping("/{id}")
    public StudentDto getStudentById(@PathVariable Long id, HttpSession session) {
        String branch = sessionService.getProfessorBranch(session);
        return studentService.getStudentByIdAndBranch(id, branch);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StudentDto createStudent(@RequestBody StudentDto studentDto, HttpSession session) {
        String branch = sessionService.getProfessorBranch(session);
        return studentService.createStudent(studentDto, branch);
    }
//update
    @PutMapping("/{id}")
    public StudentDto updateStudent(@PathVariable Long id,
                                    @RequestBody StudentDto studentDto,
                                    HttpSession session) {
        String branch = sessionService.getProfessorBranch(session);
        return studentService.updateStudent(id, studentDto, branch);
    }
// using this to find the record via id
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudent(@PathVariable Long id, HttpSession session) {
        String branch = sessionService.getProfessorBranch(session);
        studentService.deleteStudent(id, branch);
    }
}
