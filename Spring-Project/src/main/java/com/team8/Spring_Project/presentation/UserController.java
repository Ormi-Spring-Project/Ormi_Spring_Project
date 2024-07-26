package com.team8.Spring_Project.presentation;

import com.team8.Spring_Project.application.UserService;
import com.team8.Spring_Project.application.dto.UserDTO;
import com.team8.Spring_Project.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

@Controller
@RequestMapping("/v1")
public class UserController {

    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/main")
    public String getMainPage(Model model, HttpSession session) {

        Object object = session.getAttribute("login");

        if (object == null) {
            return "main";
        }

        UserDTO userDTO = (UserDTO) object;
        model.addAttribute(userDTO);
        return "main";
    }

    @GetMapping("/signup")
    public String getSignUpPage(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "signup";
    }

    @PostMapping("/signup")
    public String signUp(@ModelAttribute UserDTO userDTO) {
        userDTO = userService.signUp(userDTO);
        System.out.println("새로운 유저 정보: " + userDTO);
        if (userDTO == null) {
            return "redirect:signup";
        }
        return "redirect:main";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "signin";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute UserDTO userDTO, Model model, HttpServletRequest request) {
        userDTO = userService.login(userDTO);
        HttpSession session = request.getSession();
        if (userDTO == null) {
            return "signin";
        }

        session.setAttribute("login", userDTO);
        model.addAttribute("userDTO", userDTO);
        return "redirect:main";
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession) {
        httpSession.invalidate();
        return "redirect:main";
    }

}
