package com.example.student.service;

import com.example.student.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SessionService {

    public static final String SESSION_PROFESSOR = "loggedInProfessor";

    public void loginProfessor(HttpSession session, User user) {
        session.setAttribute(SESSION_PROFESSOR, user);
    }

    public User getLoggedInProfessor(HttpSession session) {
        User user = (User) session.getAttribute(SESSION_PROFESSOR);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Please login first");
        }
        return user;
    }

    public String getProfessorBranch(HttpSession session) {
        return getLoggedInProfessor(session).getBranch();
    }

    public void logout(HttpSession session) {
        session.invalidate();
    }
}
