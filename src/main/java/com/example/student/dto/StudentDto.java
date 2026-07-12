package com.example.student.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String branch;
    private Integer year;
}
