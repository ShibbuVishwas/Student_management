package com.example.student.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

    private boolean status;
    private boolean userExists;
    private String message;
    private String username;
    private String branch;
}
