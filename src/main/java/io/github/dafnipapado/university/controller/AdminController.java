package io.github.dafnipapado.university.controller;

import io.github.dafnipapado.university.service.ITeacherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final ITeacherService teacherService;

    @GetMapping({"", "/", "/index"})
    public String index() {
        return "admin/index";
    }

}
