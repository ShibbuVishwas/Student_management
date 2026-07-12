package com.example.student.controller;
import com.example.student.dto.LoginRequest;
import com.example.student.dto.LoginResponse;
import com.example.student.dto.RegisterRequest;
import com.example.student.dto.ProfessorSessionDto;
import com.example.student.entity.User;
import com.example.student.repository.UserRepository;
import com.example.student.service.LoginService;
import com.example.student.service.SessionService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LoginRestController {

    private final LoginService loginService;
    private final UserRepository userRepository;
    private final SessionService sessionService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request, HttpSession session) {
        LoginResponse response = loginService.validateUser(request);

        if (response.isStatus()) {
            User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
            sessionService.loginProfessor(session, user);
        }

        return response;
    }

    @GetMapping("/login")
    public LoginResponse loginWithParams(@RequestParam String username,
                                         @RequestParam String password,
                                         HttpSession session) {
        return login(new LoginRequest(username, password), session);
    }

    @GetMapping("/me")
    public ProfessorSessionDto currentProfessor(HttpSession session) {
        User user = sessionService.getLoggedInProfessor(session);
        return ProfessorSessionDto.builder()
                .username(user.getUsername())
                .branch(user.getBranch())
                .build();
    }

    @PostMapping("/logout")
    public void logout(HttpSession session) {
        sessionService.logout(session);
    }

    @PostMapping("/register")
    public LoginResponse register(@RequestBody RegisterRequest request, HttpSession session) {
        LoginResponse response = loginService.registerUser(request);

        if (response.isStatus()) {
            User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
            sessionService.loginProfessor(session, user);
        }

        return response;
    }
}
