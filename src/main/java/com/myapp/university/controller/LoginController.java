package com.myapp.university.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String getLogin(Authentication authentication, Principal principal) {
        if (principal == null) return "login";

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) return "redirect:/admin";

        boolean isTeacher = authentication.getAuthorities()
                .stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_TEACHER"));

        return (isTeacher) ? "redirect:/teachers" : "redirect:/students";
    }

    @GetMapping("/")
    public String root(Authentication authentication, Principal principal){
        if (principal == null) return "login";

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) return "redirect:/admin";

        boolean isTeacher = authentication.getAuthorities()
                .stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_TEACHER"));

        return (isTeacher) ? "redirect:/teachers" : "redirect:/students";
    }
}
