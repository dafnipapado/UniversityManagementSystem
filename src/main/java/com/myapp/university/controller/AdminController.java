package com.myapp.university.controller;

import com.myapp.university.dto.TeacherReadOnlyDTO;
import com.myapp.university.service.ITeacherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final ITeacherService teacherService;

    @GetMapping({"", "/"})
    public String index() {
        return "admin/index";
    }

}
