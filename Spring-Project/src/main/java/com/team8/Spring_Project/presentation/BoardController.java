package com.team8.Spring_Project.presentation;

import com.team8.Spring_Project.application.BoardService;
import com.team8.Spring_Project.application.CategoryService;
import com.team8.Spring_Project.application.dto.BoardDto;
import com.team8.Spring_Project.application.dto.CategoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@Controller
@RequestMapping("/v1/posts")
public class BoardController {

    private final BoardService boardService;
    private final CategoryService categoryService; // 이것도 여기서 사용하지 않고 boardService로 밀 수 있을 것 같은데.

    Logger logger = LoggerFactory.getLogger(BoardController.class);

    @Autowired
    public BoardController(BoardService boardService,
                           CategoryService categoryService) {
        this.boardService = boardService;
        this.categoryService = categoryService;
    }

    // 게시글 리스트 페이지 요청
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String getAllBoards(@RequestParam(name = "categoryId", required = false, defaultValue = "1") Long categoryId,
                               Model model,
                               HttpServletRequest request) {

        HttpSession session = request.getSession();
        UserDTO userDTO = (UserDTO) session.getAttribute("login");
        CategoryDTO categoryDto = categoryService.getCategoryById(categoryId);

        // 권한이 없으면 게시글 리스트를 못본다.
        if (userDTO == null) {
            model.addAttribute("userDTO", null);
            return "redirect:/login";
        }

        List<BoardDTO> boards = boardService.getAllBoards(categoryDto);
        List<CategoryDTO> categories = categoryService.getAllCategories();

        model.addAttribute("userDTO", userDTO);
        model.addAttribute("boards", boards);
        model.addAttribute("categories", categories); // 카테고리 목록을 모델에 추가
        model.addAttribute("selectedCategoryId", categoryId);

        return "categoryPost";
    }

    @GetMapping({"post/{id}", "notice/{id}"})
    public String getBoard(@PathVariable Long id, HttpServletRequest request, Model model) throws AccessDeniedException {

        HttpSession session = request.getSession();
        UserDTO userDTO = (UserDTO) session.getAttribute("login");
        String path = request.getRequestURI();

        BoardDto board;
        String type;

        if (path.contains("/notice/")) {
            board = boardService.getNoticeById(id, userDTO);
            type = "notice";
        } else {
            board = boardService.getPostById(id, userDTO);
            type = "post";
        }

        model.addAttribute("board", board);
        model.addAttribute("type", type);
        return "viewPost";
    }

    // 게시글 작성 페이지 요청
    @GetMapping("/write")
    @ResponseStatus(HttpStatus.OK)
    public String getWritePost(Model model,
                               HttpServletRequest request) throws AccessDeniedException {

        HttpSession session = request.getSession();
        UserDTO userDTO = (UserDTO) session.getAttribute("login");

        // 권한이 없으면 글쓰기 버튼 누를 경우 예외 던짐.
        if (userDTO.getAuthority() == Authority.BANNED) {
            throw new AccessDeniedException("권한 정지로 인해 글을 생성할 수 없습니다.");
        }

        model.addAttribute("board", new BoardDTO());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "writePost";

    }

    // 게시글 생성
    @PostMapping
    public String createPost(@ModelAttribute("board") BoardDTO boardDto, HttpServletRequest request) {

        HttpSession session = request.getSession();
        UserDTO userDTO = (UserDTO) session.getAttribute("login");
        Long categoryId = boardDto.getCategoryId();

        CategoryDTO categoryDto = categoryService.getCategoryById(categoryId);
        boardService.createBoard(boardDto, userDTO, categoryDto);

        return "redirect:/v1/posts";

    }

    }

    // 원래는 이렇게 권한에 따라 나눠야 한다고 생각한다.
    // @권한 관련
    // 이렇게 안할꺼면 BoardDto에 type 필드를 만들어서 나누는 정도? --> 좋은지 모르겠다.
    // 협업을 위해 이렇게 해놓아야 하나?
/*    @GetMapping("/{id}/user/edit")
    public String getEditUserPost(@PathVariable("id") Long id, Model model) {
        BoardDto board = boardService.getPostById(id);
        List<CategoryDto> categories = categoryService.getAllCategories();
        model.addAttribute("post", board);
        model.addAttribute("categories", categories);
        return "editPost";
    }*/

    // 원래는 이렇게 권한에 따라 나눠야 한다고 생각한다.
    // @권한 관련
    // 이렇게 안할꺼면 BoardDto에 type 필드를 만들어서 나누는 정도? --> 좋은지 모르겠다.
/*    @GetMapping("/{id}/admin/edit")
    public String getEditAdminNotice(@PathVariable("id") Long id, Model model) {
        BoardDto board = boardService.getNoticeById(id);
        List<CategoryDto> categories = categoryService.getAllCategories();
        model.addAttribute("notice", board);
        model.addAttribute("categories", categories);
        return "editPost";
    }*/

    @GetMapping("/{id}/edit")
    public String getEditPost(@PathVariable("id") Long id, Model model) {
        BoardDto board = boardService.getBoardById(id);
        List<CategoryDto> categories = categoryService.getAllCategories();
        model.addAttribute("board", board);
        model.addAttribute("categories", categories);
        return "editPost";
    }

    @PutMapping("/{id}/edit")
    public String updatePost(@PathVariable("id") Long id, @ModelAttribute BoardDto boardDto) {
        boardService.updateBoard(id, boardDto);
        return "redirect:/v1/posts";
    }

    @DeleteMapping("/{id}")
    public String deletePost(@PathVariable("id") Long id) {
        boardService.deleteBoard(id);
        return "redirect:/v1/posts";
    }


}
