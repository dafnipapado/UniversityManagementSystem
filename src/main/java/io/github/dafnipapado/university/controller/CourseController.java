package io.github.dafnipapado.university.controller;

import io.github.dafnipapado.university.dto.DepartmentReadOnlyDTO;
import io.github.dafnipapado.university.dto.course.CourseEditDTO;
import io.github.dafnipapado.university.dto.course.CourseInsertDTO;
import io.github.dafnipapado.university.dto.course.CourseReadOnlyDTO;
import io.github.dafnipapado.university.exception.EntityAlreadyExistsException;
import io.github.dafnipapado.university.exception.EntityNotFoundException;
import io.github.dafnipapado.university.service.ICourseService;
import io.github.dafnipapado.university.service.IDepartmentService;
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
@RequestMapping("/courses")
public class CourseController {

    private final ICourseService courseService;
    private final IDepartmentService departmentService;

    @GetMapping("/create")
    public String getCreateCourse(Model model){
        model.addAttribute("courseInsertDTO", CourseInsertDTO.empty());
        return "courses/course-create";
    }

    @PostMapping("/create")
    public String createCourse(@Valid @ModelAttribute("courseInsertDTO") CourseInsertDTO courseInsertDTO,
                                BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "courses/course-create";
        }
        try{
            courseService.saveCourse(courseInsertDTO);
        } catch (Exception e) {
            log.error(e.getMessage());
            return "courses/course-create";
        }
        return "redirect:/courses/success";
    }

    @GetMapping("/success")
    public String successSave(RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("successMessage", "Course was saved successfully");
        return "redirect:/courses/view";
    }

    @GetMapping("/edit/{uuid}")
    public String getEditCourse(@PathVariable UUID uuid, Model model) {
        try{
            CourseEditDTO courseEditDTO = courseService.getCourseEditDTO(uuid);
            model.addAttribute("courseEditDTO", courseEditDTO);
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
        }
        return "courses/course-edit";
    }

    @PostMapping("/edit")
    public String editCourse(@Valid @ModelAttribute CourseEditDTO courseEditDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model)
            throws EntityAlreadyExistsException, EntityNotFoundException {
        if (bindingResult.hasErrors()) {
            return "courses/course-edit";
        }
        try {
            CourseReadOnlyDTO courseReadOnlyDTO = courseService.updateCourse(courseEditDTO);
            redirectAttributes.addFlashAttribute("courseReadOnlyDTO", courseReadOnlyDTO);
        } catch (EntityAlreadyExistsException | EntityNotFoundException e) {
            log.error(e.getMessage());
            return "courses/course-edit";
        }
        return "redirect:/courses/update-success";
    }

    @GetMapping("/update-success")
    public String updateSuccess(Model model) {
        model.addAttribute("successMessage", "Course has been updated successfully.");
        return "admin/courses-view";
    }

    @PostMapping("/delete/{uuid}")
    public String delete(@PathVariable UUID uuid, RedirectAttributes redirectAttributes) {
        try{
            courseService.deleteCourse(uuid);
            redirectAttributes.addFlashAttribute("successMessage", "Course was deleted successfully");
            return "redirect:/courses/view";
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return "admin/courses-view";
        }
    }

    @GetMapping("/view")
    public String getCoursesPaginated(@PageableDefault(page = 0, size = 5, sort = "code") Pageable pageable, Model model){
        Page<CourseReadOnlyDTO> coursesPaginated = courseService.getCoursesPaginated(pageable);
        model.addAttribute("courses", coursesPaginated.getContent());
        model.addAttribute("page", coursesPaginated);
        return "admin/courses-view";
    }

    @ModelAttribute("departmentsReadOnlyDTO")
    public List<DepartmentReadOnlyDTO> departments() {
        return departmentService.getAllDepartments();
    }
}
