package com.team8.Spring_Project.presentation;

import com.team8.Spring_Project.application.BoardService;
import com.team8.Spring_Project.application.CategoryService;
import com.team8.Spring_Project.application.PostService;
import com.team8.Spring_Project.application.dto.BoardDTO;
import com.team8.Spring_Project.application.dto.CategoryDTO;
import com.team8.Spring_Project.application.dto.PostDTO;
import com.team8.Spring_Project.application.dto.UserDTO;
import com.team8.Spring_Project.domain.Authority;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("/v1/posts")
public class BoardController {

    private final BoardService boardService;
    private final CategoryService categoryService;
    private final PostService postService;

    @Autowired
    public BoardController(BoardService boardService,
                           CategoryService categoryService,
                           PostService postService) {
        this.boardService = boardService;
        this.categoryService = categoryService;
        this.postService = postService;
    }

    // 게시글 리스트 페이지 요청
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String getAllBoards(@RequestParam(name = "categoryId", required = false, defaultValue = "1") Long categoryId,
                               Model model,
                               Authentication authentication) {

        UserDTO userDTO = (UserDTO) authentication.getPrincipal();
        CategoryDTO categoryDto = categoryService.getCategoryById(categoryId);
        String categoryName = categoryDto.getName();

        List<BoardDTO> boards = boardService.getAllBoards(categoryDto);
        List<CategoryDTO> categories = categoryService.getAllCategories();

        model.addAttribute("userDTO", userDTO);
        model.addAttribute("boards", boards);
        model.addAttribute("keyword", "");
        model.addAttribute("categories", categories);
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("categoryName", categoryName);

        return "categoryPost";
    }

    // 게시글 제목 또는 작성자 기준 특정 키워드로 검색
    @GetMapping(params = {"categoryId", "keyword"})
    public String getBoardsByKeyword(@RequestParam(name = "categoryId", required = false, defaultValue = "1") Long categoryId,
                                     @RequestParam("keyword") String keyword,
                                     HttpServletRequest request,
                                     Model model) {

        HttpSession session = request.getSession();
        UserDTO userDTO = (UserDTO) session.getAttribute("login");
        CategoryDTO categoryDto = categoryService.getCategoryById(categoryId);
        String categoryName = categoryDto.getName();

        List<BoardDTO> boards = boardService.getBoardByKeyword(keyword, categoryName);
        List<CategoryDTO> categories = categoryService.getAllCategories();

        model.addAttribute("userDTO", userDTO);
        model.addAttribute("boards", boards);
        model.addAttribute("categories", categories);
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("categoryName", categoryName);

        return "categoryPost";
    }

    @GetMapping("/article-items")
    @ResponseBody
    public ResponseEntity<List<PostDTO>> getPostsByCategory(@RequestParam Long categoryId) {
        List<PostDTO> posts = postService.getAllPostsByCategory(categoryId);
        return ResponseEntity.ok(posts);
    }

    // 게시글 상세보기
    @GetMapping({"post/{id}", "notice/{id}"})
    public String getBoardById(@PathVariable Long id,
                           @RequestParam(required = false) Long categoryId,
                           HttpServletRequest request,
                           Authentication authentication,
                           Model model) {

        UserDTO userDTO = (UserDTO) authentication.getPrincipal();
        String path = request.getRequestURI();

        BoardDTO board;
        String type;

        try {
            if (path.contains("/notice/")) {
                type = "notice";
            } else {
                type = "post";
            }
            board = boardService.getBoardById(id, userDTO, type);
        } catch (AccessDeniedException e) {
            // 원래는 뭐 에러 페이지나 다른 것을 띄워줘야 할 것 같다.
            return "권한이 없습니다.";
        }

        // 본인 인증
        boolean isAuthor = userDTO.getId().equals(board.getUserId());

        // ( 본인은 본인 글만 or 관리자 ) 일 경우 수정 가능
        boolean canEdit = isAuthor || userDTO.getAuthority() == Authority.ADMIN;

        if (type.equals("post")) {
            model.addAttribute("image", Base64.getEncoder().encodeToString(board.getPicture()));
        }

        model.addAttribute("userDTO", userDTO);
        model.addAttribute("board", board);
        model.addAttribute("type", type);
        model.addAttribute("canEdit", canEdit);
        model.addAttribute("categoryId", categoryId);
        return "viewPost";
    }

    // 게시글 작성 페이지 요청
    @GetMapping("/write")
    @ResponseStatus(HttpStatus.OK)
    public String getWritePost(Model model,
                               Authentication authentication) throws AccessDeniedException {

        UserDTO userDTO = (UserDTO) authentication.getPrincipal();
        List<CategoryDTO> categoryDTOList = categoryService.getAllCategories();

        // 권한이 없으면 글쓰기 버튼 누를 경우 예외 던짐.
        if (userDTO.getAuthority() == Authority.BANNED) {
            throw new AccessDeniedException("권한 정지로 인해 글을 생성할 수 없습니다.");
        }

        model.addAttribute("userDTO", userDTO);
        model.addAttribute("board", new BoardDTO());
        model.addAttribute("categories", categoryDTOList);
        return "writePost";

    }

    // 게시글 생성
    @PostMapping
    public String createPost(@ModelAttribute("board") BoardDTO boardDto,
                             Authentication authentication,
                             @RequestPart(value = "file", required = false) MultipartFile file,
                             HttpServletRequest request) throws IOException {

        UserDTO userDTO = (UserDTO) authentication.getPrincipal();
  
        Long categoryId = boardDto.getCategoryId();

        CategoryDTO categoryDto = categoryService.getCategoryById(categoryId);

        if (userDTO.getAuthority() == Authority.USER) {
            boardDto.setPicture(file.getBytes());
        }

        boardService.createBoard(boardDto, userDTO, categoryDto);

        return "redirect:/v1/posts";

    }

    // 수정 페이지 요청
    @GetMapping({"post/{id}/edit", "notice/{id}/edit"})
    public String getEditPost(@PathVariable("id") Long id,
                              Authentication authentication,
                              HttpServletRequest request,
                              Model model) throws AccessDeniedException {

        UserDTO userDTO = (UserDTO) authentication.getPrincipal();
        String path = request.getRequestURI();

        BoardDTO board;
        String type;

        if (path.contains("/notice/")) {
            type = "notice";
        } else {
            type = "post";
        }
        board = boardService.getBoardById(id, userDTO, type);

        List<CategoryDTO> categories = categoryService.getAllCategories();

        if (type.equals("post")) {
            model.addAttribute("image", Base64.getEncoder().encodeToString(board.getPicture()));
        }

        model.addAttribute("board", board);
        model.addAttribute("type", type);
        model.addAttribute("categories", categories);
        model.addAttribute("userDTO", userDTO);

        return "editPost";

    }

    // 게시글 수정
    @PutMapping({"post/{id}/edit", "notice/{id}/edit"})
    public String updatePost(@PathVariable("id") Long id,
                             @ModelAttribute("board") BoardDTO boardDto,
                             @RequestPart(value = "file", required = false) MultipartFile file,
                             HttpSession session,
                             HttpServletRequest request) throws IOException {

        String path = request.getRequestURI();
        String type;

        if (path.contains("/notice/")) {
            type = "notice";
        } else {
            type = "post";
        }

        if (file == null) {
            UserDTO userDTO = (UserDTO) session.getAttribute("login");
            BoardDTO temp = boardService.getBoardById(id, userDTO, type);
            boardDto.setPicture(temp.getPicture());
        } else {
            boardDto.setPicture(file.getBytes());
        }

        boardService.updateBoard(id, boardDto, type);

        if (type.equals("notice")) {
            return "redirect:/v1/posts/notice/" + id;
        }

        return "redirect:/v1/posts/post/" + id;
    }

    // 게시글 삭제
    @DeleteMapping({"post/{id}", "notice/{id}"})
    public String deletePost(@PathVariable("id") Long id,
                             HttpServletRequest request) {

        String path = request.getRequestURI();
        String type;

        if (path.contains("/notice/")) {
            type = "notice";
            boardService.deleteBoard(id, type);
        } else {
            type = "post";
            boardService.deleteBoard(id, type);
        }

        return "redirect:/v1/posts";
    }


}
