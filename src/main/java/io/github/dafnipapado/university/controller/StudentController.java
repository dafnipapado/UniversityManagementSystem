package io.github.dafnipapado.university.controller;

import io.github.dafnipapado.university.dto.RegionReadOnlyDTO;
import io.github.dafnipapado.university.dto.student.StudentEditDTO;
import io.github.dafnipapado.university.dto.student.StudentInsertDTO;
import io.github.dafnipapado.university.dto.student.StudentReadOnlyDTO;
import io.github.dafnipapado.university.exception.EntityAlreadyExistsException;
import io.github.dafnipapado.university.exception.EntityNotFoundException;
import io.github.dafnipapado.university.service.IRegionService;
import io.github.dafnipapado.university.service.IStudentService;
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
@RequestMapping("/students")
public class StudentController {

    private final IStudentService studentService;
    private final IRegionService regionService;

    @GetMapping({"", "/", "/index"})
    public String index(Model model){
        try{
            StudentReadOnlyDTO studentReadOnlyDTO = studentService.getIndex();
            model.addAttribute("studentReadOnlyDTO", studentReadOnlyDTO);
        } catch(EntityNotFoundException e) {
//            e.getMessage();
            //return error page
        }
        return "/students/index";
    }

    @GetMapping("/create")
    public String getCreateStudent(Model model){
        model.addAttribute("studentInsertDTO", StudentInsertDTO.empty());
        model.addAttribute("regionReadOnlyDTO", regionService.getAllRegions());
        return "students/student-create";
    }

    @PostMapping("/create")
    public String createStudent(@Valid @ModelAttribute("studentInsertDTO") StudentInsertDTO dto,
                                BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "students/student-create";
        }
        try{
            studentService.saveStudent(dto);
        } catch (Exception e) {
            return "students/student-create";
        }
        return "redirect:/students/success";
    }

    @GetMapping("/success")
    public String successSave(RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("successMessage", "Your info was saved successfully");
        return "redirect:/students/index";
    }

    @GetMapping("/edit/{uuid}")
    public String getEditStudent(@PathVariable UUID uuid, Model model) {
        try{
            StudentEditDTO studentEditDTO = studentService.getStudentEditDTO(uuid);
            model.addAttribute("studentEditDTO", studentEditDTO);
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
        }
        return "students/student-edit";
    }


    @PostMapping("/edit")
    public String editStudent(@Valid @ModelAttribute StudentEditDTO studentEditDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model)
            throws EntityAlreadyExistsException, EntityNotFoundException {
        if (bindingResult.hasErrors()) {
            return "students/student-edit";
        }
        try {
            StudentReadOnlyDTO studentReadOnlyDTO = studentService.updateStudent(studentEditDTO);
            redirectAttributes.addFlashAttribute("studentReadOnlyDTO", studentReadOnlyDTO);
        } catch (EntityAlreadyExistsException | EntityNotFoundException e) {
            log.error(e.getMessage());
            return "students/student-edit";
        }
        return "redirect:/students/update-success";
    }

    @GetMapping("/update-success")
    public String updateSuccess(Model model) {
        model.addAttribute("successMessage", "Personal info has been updated successfully.");
        return "students/index";
    }

    @PostMapping("/delete/{uuid}")
    public String delete(@PathVariable UUID uuid, RedirectAttributes redirectAttributes) {
        try{
            studentService.deleteStudent(uuid);
            redirectAttributes.addFlashAttribute("successMessage", "Student deleted successfully");
            return "redirect:/students/view";
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            return "admin/students-view";
        }
    }

    @GetMapping("/view")
    public String getStudentsPaginated(@PageableDefault(page = 0, size = 5, sort = "studentAM") Pageable pageable, Model model){
        Page<StudentReadOnlyDTO> studentsPaginated = studentService.getStudentsPaginated(pageable);
        model.addAttribute("students", studentsPaginated.getContent());
        model.addAttribute("page", studentsPaginated);
        return "admin/students-view";
    }


    @ModelAttribute("regionsReadOnlyDTO")
    public List<RegionReadOnlyDTO> regions() {
        return regionService.getAllRegions();
    }
}
