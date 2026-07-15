package io.github.dafnipapado.university.controller;

import io.github.dafnipapado.university.dto.course.CourseReadOnlyDTO;
import io.github.dafnipapado.university.dto.course_offering.CourseOfferingEditDTO;
import io.github.dafnipapado.university.dto.course_offering.CourseOfferingInsertDTO;
import io.github.dafnipapado.university.dto.course_offering.CourseOfferingReadOnlyDTO;
import io.github.dafnipapado.university.dto.teacher.TeacherReadOnlyDTO;
import io.github.dafnipapado.university.exception.EntityAlreadyExistsException;
import io.github.dafnipapado.university.exception.EntityNotFoundException;
import io.github.dafnipapado.university.model.Semester;
import io.github.dafnipapado.university.service.ICourseOfferingService;
import io.github.dafnipapado.university.service.ICourseService;
import io.github.dafnipapado.university.service.ISemesterService;
import io.github.dafnipapado.university.service.ITeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/course-offerings")
public class CourseOfferingController {

    private final ICourseOfferingService courseOfferingService;
    private final ICourseService courseService;
    private final ITeacherService teacherService;
    private final ISemesterService semesterService;

    @GetMapping("/create")
    public String getCreateCourseOffering(Model model){
        model.addAttribute("courseOfferingInsertDTO", CourseOfferingInsertDTO.empty());
        return "course_offerings/course-offering-create";
    }

    @PostMapping("/create")
    public String createCourseOffering(@Valid @ModelAttribute("courseOfferingInsertDTO") CourseOfferingInsertDTO courseOfferingInsertDTO,
                                BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "course_offerings/course-offering-create";
        }
        try{
            courseOfferingService.saveCourseOffering(courseOfferingInsertDTO);
        } catch (Exception e) {
            return "course_offerings/course-offering-create";
        }
        return "redirect:/course-offerings/view";
    }

    @GetMapping("/edit/{uuid}")
    public String getEditCourseOffering(@PathVariable UUID uuid, Model model) {
        try{
            CourseOfferingEditDTO courseOfferingEditDTO = courseOfferingService.getCourseOfferingEditDTO(uuid);
            model.addAttribute("courseOfferingEditDTO", courseOfferingEditDTO);
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
        }
        return "course_offerings/course-offering-edit";
    }

    @PostMapping("/edit")
    public String editCourseOffering(@Valid @ModelAttribute CourseOfferingEditDTO courseOfferingEditDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model)
            throws EntityAlreadyExistsException, EntityNotFoundException {
        if (bindingResult.hasErrors()) {
            return "course_offerings/course-offering-edit";
        }
        try {
            CourseOfferingReadOnlyDTO courseOfferingReadOnlyDTO = courseOfferingService.updateCourseOffering(courseOfferingEditDTO);
            redirectAttributes.addFlashAttribute("courseOfferingReadOnlyDTO", courseOfferingReadOnlyDTO);
        } catch (EntityAlreadyExistsException | EntityNotFoundException e) {
            log.error(e.getMessage());
            return "course_offerings/course-offering-edit";
        }
        return "redirect:/course-offerings/view";
    }

    @PostMapping("/delete/{uuid}")
    public String deleteCourseOffering(@PathVariable UUID uuid, RedirectAttributes redirectAttributes) {
        try{
            courseOfferingService.deleteCourseOffering(uuid);
            redirectAttributes.addFlashAttribute("successMessage", "Course offering deleted successfully");
            return "redirect:/course-offerings/view";
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return "course_offerings/course-offerings-view";
        }
    }

    @GetMapping("/view")
    public String getCourseOfferingsPaginated(@PageableDefault(page = 0, size = 5, sort = {"deleted" , "course.department.name"}) Pageable pageable, Model model){
        Page<CourseOfferingReadOnlyDTO> courseOfferingsPaginated = courseOfferingService.getCourseOfferingsPaginated(pageable);
        model.addAttribute("courseOfferings", courseOfferingsPaginated.getContent());
        model.addAttribute("page", courseOfferingsPaginated);
        return "course_offerings/course-offerings-view";
    }

    @ModelAttribute("coursesList")
    public List<CourseReadOnlyDTO> courses() {
        return courseService.getAllCourses();
    }

    @ModelAttribute("teachersList")
    public List<TeacherReadOnlyDTO> teachers() {
        return teacherService.getAllTeachers();
    }

    @ModelAttribute("semestersList")
    public List<Semester> semesters() {
        return semesterService.getAllSemesters();
    }
}
