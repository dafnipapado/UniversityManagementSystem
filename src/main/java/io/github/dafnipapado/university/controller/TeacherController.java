package io.github.dafnipapado.university.controller;

import io.github.dafnipapado.university.dto.RegionReadOnlyDTO;
import io.github.dafnipapado.university.dto.teacher.TeacherEditDTO;
import io.github.dafnipapado.university.dto.teacher.TeacherInsertDTO;
import io.github.dafnipapado.university.dto.teacher.TeacherReadOnlyDTO;
import io.github.dafnipapado.university.dto.userInfo.UserInfoInsertDTO;
import io.github.dafnipapado.university.exception.EntityAlreadyExistsException;
import io.github.dafnipapado.university.exception.EntityNotFoundException;
import io.github.dafnipapado.university.service.IRegionService;
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
@RequestMapping("/teachers")
public class TeacherController {

    private final ITeacherService teacherService;
    private final IRegionService regionService;

    @GetMapping({"", "/", "/index"})
    public String index(Model model){
        try{
            TeacherReadOnlyDTO teacherReadOnlyDTO = teacherService.getIndex();
            model.addAttribute("teacherReadOnlyDTO", teacherReadOnlyDTO);
        } catch(EntityNotFoundException e) {
//            e.getMessage();
            //return error page
        }
        return "/teachers/index";
    }

    @GetMapping("/create")
    public String getCreateTeacher(Model model){
        model.addAttribute("teacherInsertDTO", TeacherInsertDTO.empty());
        model.addAttribute("regionReadOnlyDTO", regionService.getAllRegions());
        return "teachers/teacher-create";
    }

    @PostMapping("/create")
    public String createTeacher(@Valid @ModelAttribute("teacherInsertDTO") TeacherInsertDTO dto,
                              BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "teachers/teacher-create";
        }
        try{
            teacherService.saveTeacher(dto);
        } catch (Exception e) {
            return "teachers/teacher-create";
        }
        return "redirect:/teachers/success";
    }

    @GetMapping("/success")
    public String successSave(RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("successMessage", "Your info was saved successfully");
        return "redirect:/teachers/index";
    }

    @GetMapping("/edit/{uuid}")
    public String getEditTeacher(@PathVariable UUID uuid, Model model) {
        try{
            TeacherEditDTO teacherEditDTO = teacherService.getTeacherEditDTO(uuid);
            model.addAttribute("teacherEditDTO", teacherEditDTO);
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
        }
        return "teachers/teacher-edit";
    }


    @PostMapping("/edit")
    public String editTeacher(@Valid @ModelAttribute TeacherEditDTO teacherEditDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) throws EntityAlreadyExistsException, EntityNotFoundException {
        if (bindingResult.hasErrors()) {
            return "teachers/teacher-edit";
        }
        try {
            TeacherReadOnlyDTO teacherReadOnlyDTO = teacherService.updateTeacher(teacherEditDTO);
            redirectAttributes.addFlashAttribute("teacherReadOnlyDTO", teacherReadOnlyDTO);
        } catch (EntityAlreadyExistsException | EntityNotFoundException e) {
            log.error(e.getMessage());
            return "teachers/teacher-edit";
        }
        return "redirect:/teachers/update-success";
    }

    @GetMapping("/update-success")
    public String updateSuccess(Model model) {
        model.addAttribute("successMessage", "Personal info has been updated successfully.");
        return "teachers/index";
    }

    @PostMapping("/delete/{uuid}")
    public String delete(@PathVariable UUID uuid, RedirectAttributes redirectAttributes) {
        try{
            teacherService.deleteTeacher(uuid);
            redirectAttributes.addFlashAttribute("successMessage", "Teacher deleted successfully");
            return "redirect:/teachers/view";
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return "admin/teachers-view";
        }

    }

    @GetMapping("/view")
    public String getTeachersPaginated(@PageableDefault(page = 0, size = 5, sort = "teacherAM") Pageable pageable, Model model){
        Page<TeacherReadOnlyDTO> teachersPaginated = teacherService.getTeachersPaginated(pageable);
        model.addAttribute("teachers", teachersPaginated.getContent());
        model.addAttribute("page", teachersPaginated);
        return "admin/teachers-view";
    }


    @ModelAttribute("regionsReadOnlyDTO")
    public List<RegionReadOnlyDTO> regions() {
        return regionService.getAllRegions();
    }

}
