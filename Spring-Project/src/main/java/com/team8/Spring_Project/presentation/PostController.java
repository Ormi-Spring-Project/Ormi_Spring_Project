package com.team8.Spring_Project.presentation;

import com.team8.Spring_Project.application.BoardService;
import com.team8.Spring_Project.application.CategoryService;
import com.team8.Spring_Project.application.dto.BoardDto;
import com.team8.Spring_Project.application.dto.PostDto;
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
public class PostController {

    private final BoardService boardService;
    private final CategoryService categoryService;

    Logger logger = LoggerFactory.getLogger(PostController.class);

    @Autowired
    public PostController(BoardService boardService,
                          CategoryService categoryService) {
        this.boardService = boardService;
        this.categoryService = categoryService;
    }


    // 게시글 리스트 페이지 요청
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String getAllPosts(Model model) {
        List<BoardDto> boards = boardService.getAllBoards();
        model.addAttribute("boards", boards);
        logger.info("Number of boards retrieved: {}", boards.size());
        return "categoryPost";
    }

    // 게시글 상세보기 요청
    @GetMapping("/{id}")
    public String getPost(@PathVariable("id") Long id,
                          Model model,
                          @ModelAttribute("post") PostDto postDto) {

        BoardDto board = boardService.getBoardById(id);
        model.addAttribute("board", board);

        return "viewPost";
    }


    // 게시글 작성 페이지 요청
    @GetMapping("/write")
    @ResponseStatus(HttpStatus.OK)
    public String getWritePost(Model model) {
        model.addAttribute("board", new BoardDto());
        model.addAttribute("categories", categoryService.getAllCategories());
        logger.info("Received UserID: {}", categoryService.getAllCategories());
        return "writePost";
    }

    // User가 새로운 게시글 작성 요청
    @PostMapping
    // @ResponseStatus(HttpStatus.CREATED) 무지성으로 이거 붙였는데 이게 리다이렉트 시 빈 화면이 뜨는 주범이었습니다.
    public String createPost(@ModelAttribute("board") BoardDto boardDto) {

        try {
            boardService.createBoard(boardDto, "ADMIN");
            logger.info("Board created successfully, redirecting to /v1/posts");
            return "redirect:/v1/posts";
        } catch (Exception e) {
            logger.error("Error occurred while creating post", e);
            return "redirect:/v1/posts/write";
        }

    }



}
