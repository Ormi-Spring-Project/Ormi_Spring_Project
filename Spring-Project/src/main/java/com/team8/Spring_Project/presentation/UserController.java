package com.team8.Spring_Project.presentation;

import com.team8.Spring_Project.application.UserService;
import com.team8.Spring_Project.application.dto.UserDTO;
import com.team8.Spring_Project.domain.Authority;
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
    public UserController(UserService userService, HttpServletRequest request) {
        this.userService = userService;
    }

    @GetMapping("/main")
    public String getMainPage(Model model, HttpServletRequest request) {

        HttpSession session = request.getSession();
        UserDTO userDTO  = (UserDTO) session.getAttribute("login");

        if (userDTO == null) {
            System.out.println("비어있는데용~");
            model.addAttribute("userDTO", null);
            return "main";
        }

        model.addAttribute("userDTO", userDTO);
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
    public String login(@ModelAttribute UserDTO userDTO, HttpServletRequest request) {
        UserDTO loginedUserDTO = userService.login(userDTO);

        if (loginedUserDTO == null) {
            return "redirect:login";
        }

        HttpSession session = request.getSession();
        session.setAttribute("login", loginedUserDTO);

        return "redirect:main";
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession) {
        httpSession.invalidate();
        return "redirect:main";
    }

    @GetMapping("/user/{id}")
    public String getMyPage(@PathVariable("id") Long id, Model model) {
        UserDTO userDTO = userService.findUser(id);
        model.addAttribute("userDTO", userDTO);
        return "mypage";
    }

    @GetMapping("/admin")
    public String getAdminPage(HttpSession httpSession) {
        UserDTO userDTO = (UserDTO) httpSession.getAttribute("login");

        if (userDTO.getAuthority() != Authority.ADMIN) {
            return "redirect:main";
        }

        return "admin";
    }

}
