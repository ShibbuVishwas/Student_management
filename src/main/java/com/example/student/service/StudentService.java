package com.example.student.service;

import com.example.student.dto.StudentDto;
import com.example.student.entity.Student;
import com.example.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public List<StudentDto> getStudentsByBranch(String branch) {
        return studentRepository.findByBranch(branch).stream()
                .map(this::toDto)
                .toList();
    }

    public StudentDto getStudentByIdAndBranch(Long id, String branch) {
        Student student = studentRepository.findByIdAndBranch(id, branch)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Student not found in your branch"));
        return toDto(student);
    }

    public StudentDto createStudent(StudentDto dto, String professorBranch) {
        Student student = toEntity(dto);
        student.setBranch(professorBranch);
        return toDto(studentRepository.save(student));
    }

    public StudentDto updateStudent(Long id, StudentDto dto, String professorBranch) {
        Student student = studentRepository.findByIdAndBranch(id, professorBranch)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Student not found in your branch"));

        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setEmail(dto.getEmail());
        student.setYear(dto.getYear());
        student.setBranch(professorBranch);

        return toDto(studentRepository.save(student));
    }

    public void deleteStudent(Long id, String professorBranch) {
        Student student = studentRepository.findByIdAndBranch(id, professorBranch)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Student not found in your branch"));
        studentRepository.delete(student);
    }

    private StudentDto toDto(Student student) {
        return StudentDto.builder()
                .id(student.getId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .branch(student.getBranch())
                .year(student.getYear())
                .build();
    }

    private Student toEntity(StudentDto dto) {
        Student student = new Student();
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setEmail(dto.getEmail());
        student.setBranch(dto.getBranch());
        student.setYear(dto.getYear());
        return student;
    }
}
