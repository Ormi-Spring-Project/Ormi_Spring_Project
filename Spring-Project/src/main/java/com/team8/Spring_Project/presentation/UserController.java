package com.team8.Spring_Project.presentation;

import com.team8.Spring_Project.application.UserService;
import com.team8.Spring_Project.application.dto.UserDTO;
import com.team8.Spring_Project.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/v1")
public class UserController {

    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/main")
    public String getMainPage() {
        return "main";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "signin";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute UserDTO userDTO) {
        userDTO = userService.login(userDTO);
        if (userDTO == null) {
            return "redirect:login";
        }
        return "redirect:main";
    }

}
