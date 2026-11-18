package io.github.dafnipapado.university.controller;

import io.github.dafnipapado.university.dto.examination.ExaminationInsertDTO;
import io.github.dafnipapado.university.service.IExaminationService;
import io.github.dafnipapado.university.service.IUtilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/examinations")
public class ExaminationController {

    private final IExaminationService examinationService;
    private final IUtilityService utilityService;

    @GetMapping("/create/{courseOfferingUuid}")
    public String getCreateExamination(Model model, @PathVariable UUID courseOfferingUuid) {
        model.addAttribute("examinationInsertDTO", ExaminationInsertDTO.empty(courseOfferingUuid));
        return "shared/examination/create";
    }

    @PostMapping("/create")
    public String createExamination(@Valid @ModelAttribute("examinationInsertDTO") ExaminationInsertDTO examinationInsertDTO,
                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return utilityService.getTemplateByRole();
        }
        try {
            examinationService.saveExamination(examinationInsertDTO);
        } catch (Exception e) {
            return utilityService.getTemplateByRole();
        }
        return utilityService.getTemplateByRole();
    }
}
