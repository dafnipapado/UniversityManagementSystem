package com.myapp.university.controller;

import com.myapp.university.dto.UserInsertDTO;
import com.myapp.university.dto.UserReadOnlyDTO;
import com.myapp.university.exception.EntityAlreadyExistsException;
import com.myapp.university.exception.EntityNotFoundException;
import com.myapp.university.mapper.Mapper;
import com.myapp.university.service.IRoleService;
import com.myapp.university.service.IUserService;
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
@RequestMapping("/users")
public class UserController {
    private final IRoleService roleService;
    private final IUserService userService;

    @GetMapping("/create")
    public String getCreateUser(Model model) {
        model.addAttribute("userInsertDTO", UserInsertDTO.empty());
        model.addAttribute("roleReadOnlyDTO", roleService.getAllRoles());
        return "/admin/user-create";
    }

    @PostMapping("/create")
    public String createUser(@Valid @ModelAttribute("teacherInsertDTO") UserInsertDTO userInsertDTO, RedirectAttributes redirectAttributes, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "admin/user-create";
        }

        try{
            UserReadOnlyDTO userReadOnlyDTO = userService.save(userInsertDTO);
            redirectAttributes.addFlashAttribute("userReadOnlyDTO", userReadOnlyDTO);
            return "redirect:/users/success";
        } catch (EntityAlreadyExistsException | EntityNotFoundException e) {
            return "/admin/user-create";
        }
    }

    @GetMapping("/success")
    public String success(Model model) {
        model.addAttribute("successMessage", "User was saved successfully");
        return "/admin/index";
    }


}
