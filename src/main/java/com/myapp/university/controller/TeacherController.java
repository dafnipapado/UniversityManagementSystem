package com.myapp.university.controller;

import com.myapp.university.dto.RegionReadOnlyDTO;
import com.myapp.university.dto.TeacherInsertDTO;
import com.myapp.university.service.IRegionService;
import com.myapp.university.service.ITeacherService;
import com.myapp.university.service.RegionServiceImpl;
import com.myapp.university.service.TeacherServiceImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/teachers")
public class TeacherController {

    private final ITeacherService teacherService;
    private final IRegionService regionService;

    @GetMapping({"", "/"})
    public String index(){
        return "/teachers/index";
    }

    @GetMapping("/getTeacherForm")
    public String viewTeachers(Model model){
        model.addAttribute("teacherInsertDTO", TeacherInsertDTO.empty());
        model.addAttribute("regionReadOnlyDTO", regionService.getAllRegions());
        return "admin/teacher-insert";
    }

    @PostMapping("/register")
    public String saveTeacher(@Valid @ModelAttribute("teacherInsertDTO") TeacherInsertDTO dto,
                              BindingResult bindingResult, Model model) {
//        System.out.println("In the controller");
//        log.error("REACHED CONTROLLER");
        if (bindingResult.hasErrors()) {
//            log.error("Validation errors: " + bindingResult.getAllErrors());
            return "admin/teacher-insert";
        }
        model.addAttribute("teacherInsertDTO", dto);
        try{
//            log.error("username: " + dto.username());
            teacherService.saveTeacher(dto);
//            log.error("Teacher saved successfully");
        } catch (Exception e) {
            return "admin/teacher-insert";
        }
        return "redirect:/teachers/success";
    }

    @GetMapping("/success")
    public String successSave(Model model){
        return "teachers/success";
    }

    @ModelAttribute("regionsReadOnlyDTO")
    public List<RegionReadOnlyDTO> regions() {
        return regionService.getAllRegions();
    }
}
