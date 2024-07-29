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
    public String getAllBoards(Model model, HttpServletRequest request) {

        HttpSession session = request.getSession();
        UserDTO userDTO = (UserDTO) session.getAttribute("login");

        // 권한이 없으면 게시글 리스트를 못본다.
        if (userDTO == null) {
            model.addAttribute("userDTO", null);
            return "redirect:/login";
        }

        model.addAttribute("userDTO", userDTO);

        List<BoardDto> boards = boardService.getAllBoards();
        model.addAttribute("boards", boards);

        return "categoryPost";
    }

    // 게시글 상세보기 요청
    @GetMapping("/{id}")
    public String getBoard(@PathVariable("id") Long id, Model model) {

        BoardDto board = boardService.getBoardById(id);

        model.addAttribute("board", board);

        return "viewPost";

    }

    // 게시글 상세보기 요청
    // 이런식으로 권한으로 나누거나 post/notice로 나누는게 좋아보인다.
/*    @GetMapping("/{id}/user")
    public String getPost(@PathVariable("id") Long id,
                          Model model) {

        BoardDto board = boardService.getPostById(id);

        model.addAttribute("board", board);

        return "viewPost";
    }*/

/*    @GetMapping("/{id}/admin")
    public String getNotice(@PathVariable("id") Long id,
                          Model model) {

        BoardDto board = boardService.getNoticeById(id);

        model.addAttribute("board", board);

        return "viewPost";
    }*/

    // 게시글 작성 페이지 요청
    @GetMapping("/write")
    @ResponseStatus(HttpStatus.OK)
    public String getWritePost(Model model) {
        model.addAttribute("board", new BoardDto());

        // 이거 카테고리 Entity -> Dto 변환 안했는데?
        model.addAttribute("categories", categoryService.getAllCategories());
        logger.info("Received UserID: {}", categoryService.getAllCategories());
        return "writePost";
    }

    // User가 새로운 게시글 작성 요청
    @PostMapping
    // @ResponseStatus(HttpStatus.CREATED) 무지성으로 이거 붙였는데 이게 리다이렉트 시 빈 화면이 뜨는 주범이었습니다.
    public String createPost(@ModelAttribute("board") BoardDto boardDto) {

        try {
            boardService.createBoard(boardDto, "USER");
            logger.info("Board created successfully, redirecting to /v1/posts");
            return "redirect:/v1/posts";
        } catch (Exception e) {
            logger.error("Error occurred while creating post", e);
            return "redirect:/v1/posts/write";
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
