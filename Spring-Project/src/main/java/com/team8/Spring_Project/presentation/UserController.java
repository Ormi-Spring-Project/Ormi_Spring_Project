package com.team8.Spring_Project.presentation;

import com.team8.Spring_Project.application.CategoryService;
import com.team8.Spring_Project.application.PostService;
import com.team8.Spring_Project.application.UserService;
import com.team8.Spring_Project.application.dto.CategoryDTO;
import com.team8.Spring_Project.application.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/v1")
public class UserController {

    UserService userService;
    CategoryService categoryService;
    PostService postService;

    @Autowired
    public UserController(UserService userService,
                          CategoryService categoryService,
                          PostService postService) {
        this.userService = userService;
        this.categoryService = categoryService;
        this.postService = postService;
    }

    @GetMapping("/main")
    public String getMainPage(Model model, Authentication authentication) {

        List<CategoryDTO> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);

        if(authentication == null) {
            // 해당 필요한 작업 수행
        }

        // 기존의 session 객체를 authentication 이 대체
        if (authentication != null && authentication.isAuthenticated()) {
            UserDTO userDTO = (UserDTO) authentication.getPrincipal();
            model.addAttribute("userDTO", userDTO);
        }

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
    public String getLoginPage() {
        return "signin";
    }

    // Spring Security가 login, logout 담당하므로 login, logout 기능 삭제 처리.

    @GetMapping("/user/{id}")
    // 여기는 My Page이기 때문에 id 검사가 필요해 메서드 수준에서 2차 검증.
    @PreAuthorize("authentication.principal.id == #id and hasRole('USER')")
    public String getMyPage(@PathVariable("id") Long id,
                            Model model) {

        UserDTO userDTO = userService.findUser(id);
        model.addAttribute("userDTO", userDTO);

        return "mypage";
    }

    @PutMapping("/user/{id}")
    // MyPage에 들어갈 때 검증을 했더라도 업데이트 시 또 검증 해주는게 보안상 좋다.
    @PreAuthorize("authentication.principal.id == #id and hasRole('USER')")
    public String updateMyInformation(@PathVariable("id") Long id,
                                      @ModelAttribute UserDTO userDTO) {

        // id와 userDTO.getId()가 일치하는지 추가 검증
        if (!id.equals(userDTO.getId())) {
            throw new AccessDeniedException("수정할 권한이 없습니다.");
        }

        userService.updateUser(userDTO);
        return "redirect:/v1/user/" + userDTO.getId();
    }

    @DeleteMapping("/user/{id}")
    @PreAuthorize("authentication.principal.id == #id or hasRole('USER') or hasRole('ADMIN')")
    public String deleteUser(@ModelAttribute UserDTO userDTO) {
        userService.deleteUser(userDTO);
        return "redirect:/v1/main";
    }

    @GetMapping("/admin")
    public String getAdminPage(Model model) {
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
