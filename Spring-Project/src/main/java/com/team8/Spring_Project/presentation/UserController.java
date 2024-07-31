package com.team8.Spring_Project.presentation;

import com.team8.Spring_Project.application.UserService;
import com.team8.Spring_Project.application.dto.UserDTO;
import com.team8.Spring_Project.domain.Authority;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/v1")
public class UserController {

    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/main")
    public String getMainPage(Model model, HttpServletRequest request) {

        HttpSession session = request.getSession();
        UserDTO userDTO = (UserDTO) session.getAttribute("login");

        if (userDTO == null) {
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
        return "redirect:/v1/main";
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
            return "redirect:/v1/login";
        }

        HttpSession session = request.getSession();
        session.setAttribute("login", loginedUserDTO);

        return "redirect:/v1/main";
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession) {
        httpSession.invalidate();
        return "redirect:/v1/main";
    }

    @GetMapping("/user/{id}")
    public String getMyPage(@PathVariable("id") Long id, Model model) {
        UserDTO userDTO = userService.findUser(id);
        model.addAttribute("userDTO", userDTO);
        return "mypage";
    }

    @PutMapping("/user/{id}")
    public String updateMyInformation(@ModelAttribute UserDTO userDTO) {
        userService.updateUser(userDTO);
        return "redirect:/v1/user/" + userDTO.getId();
    }

    @DeleteMapping("/user/{id}")
    public String deleteUser(@ModelAttribute UserDTO userDTO, HttpSession httpSession) {
        userService.deleteUser(userDTO);
        httpSession.invalidate();
        return "redirect:/v1/main";
    }

    @GetMapping("/admin")
    public String getAdminPage(HttpSession httpSession, Model model) {
        UserDTO userDTO = (UserDTO) httpSession.getAttribute("login");

        if (userDTO.getAuthority() != Authority.ADMIN) {
            return "redirect:/v1/main";
        }

        List<UserDTO> userDTOList = userService.getAllUsers();
        model.addAttribute("userList", userDTOList);

        return "admin";
    }

    @PutMapping("/admin/{user_id}")
    public String changeUserAuthority(@PathVariable("user_id") Long id) {
        userService.changeUserAuthority(id);
        return "redirect:/v1/admin";
    }

}
