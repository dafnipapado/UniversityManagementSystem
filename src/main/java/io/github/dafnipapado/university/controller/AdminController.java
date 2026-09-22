package io.github.dafnipapado.university.controller;

import io.github.dafnipapado.university.service.ICourseOfferingService;
import io.github.dafnipapado.university.service.ICourseService;
import io.github.dafnipapado.university.service.ITeacherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final ITeacherService teacherService;
    private final ICourseOfferingService courseOfferingService;
    private final ICourseService courseService;

    @GetMapping({"", "/", "/index"})
    public String index(Model model) {
        model.addAttribute("courseOfferingsCount", courseOfferingService.getActiveCourseOfferingCount());
        model.addAttribute("latestCourseOffering", courseOfferingService.getLatestCourseOffering());
        model.addAttribute("coursesCount", courseService.countCoursesDeletedFalse());
        model.addAttribute("latestCourse", courseService.getLatestCourse());
        return "admin/index";
    }

}
