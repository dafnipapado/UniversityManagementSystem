package io.github.dafnipapado.university.controller;

import io.github.dafnipapado.university.dto.semester.SemesterInsertDTO;
import io.github.dafnipapado.university.service.ISemesterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/semesters")
public class SemesterController {

    private final ISemesterService semesterService;

    @GetMapping("/create")
    public String getCreateSemester(Model model){
        model.addAttribute("semesterInsertDTO", SemesterInsertDTO.empty());
        return "admin/semester-create";
    }

    @PostMapping("/create")
    public String createSemester(@Valid @ModelAttribute("semesterInsertDTO") SemesterInsertDTO semesterInsertDTO,
                               BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "admin/semester-create";
        }
        try{
            semesterService.saveSemester(semesterInsertDTO);
        } catch (Exception e) {
            log.error(e.getMessage());
            return "admin/semester-create";
        }
        return "redirect:/semesters/success";
    }

    @GetMapping("/success")
    public String successSave(RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("successMessage", "Semester was saved successfully");
        return "redirect:/admin/index";
    }
}
