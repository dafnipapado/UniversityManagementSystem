package com.myapp.university.controller;

import com.myapp.university.dto.RegionReadOnlyDTO;
import com.myapp.university.dto.TeacherEditDTO;
import com.myapp.university.dto.TeacherInsertDTO;
import com.myapp.university.dto.TeacherReadOnlyDTO;
import com.myapp.university.exception.EntityAlreadyExistsException;
import com.myapp.university.exception.EntityNotFoundException;
import com.myapp.university.service.IRegionService;
import com.myapp.university.service.ITeacherService;
import com.myapp.university.service.RegionServiceImpl;
import com.myapp.university.service.TeacherServiceImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/teachers")
public class TeacherController {

    private final ITeacherService teacherService;
    private final IRegionService regionService;

    @GetMapping({"", "/"})
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
    public String successSave(Model model){
        model.addAttribute("successMessage", "Your info was saved successfully");
        return "/teachers/index";
    }

    @GetMapping("/edit/{uuid}")
    public String getEditTeacher(@PathVariable UUID uuid, Model model) {
        try{
            TeacherEditDTO teacherEditDTO = teacherService.findTeacherByUuid(uuid);
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
