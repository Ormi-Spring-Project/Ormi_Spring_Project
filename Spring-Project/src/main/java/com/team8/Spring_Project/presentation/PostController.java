package com.team8.Spring_Project.presentation;

import com.team8.Spring_Project.application.BoardService;
import com.team8.Spring_Project.application.NoticeService;
import com.team8.Spring_Project.application.PostService;
import com.team8.Spring_Project.application.dto.BoardDto;
import com.team8.Spring_Project.application.dto.PostDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/v1/posts")
public class PostController {

    private final BoardService boardService;
    private final PostService postService;
    private final NoticeService noticeService;

    @Autowired
    public PostController(BoardService boardService, PostService postService, NoticeService noticeService) {
        this.boardService = boardService;
        this.postService = postService;
        this.noticeService = noticeService;
    }

    // 게시글 리스트 페이지 요청
    // 아니면 getBoards?
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String getAllPosts(Model model) {
        List<BoardDto> boards = boardService.getAllBoards();
        model.addAttribute("boards", boards);
        return "categoryPost";
    }

    // 게시글 작성 페이지 요청
    @GetMapping("/write")
    @ResponseStatus(HttpStatus.OK)
    public String getWritePost(Model model) {
        model.addAttribute("board", new PostDto());
        return "writePost";
    }

    // User가 새로운 게시글 작성 요청
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createPost(@ModelAttribute BoardDto boardDto) {
        // PostDto createPost = postService.createPost(postDto);

        // BoardDto의 필드 값을 로그로 확인
        System.out.println("Title: " + boardDto.getTitle());
        System.out.println("Application: " + boardDto.getApplication());
        System.out.println("Tag: " + boardDto.getTag());
        System.out.println("Content: " + boardDto.getContent());

        // authority 파라미터에 유저의 권한을 넣어야하는데 이걸 컨트롤러에서 가능한가?
        //boardService.createBoard(boardDto, "USER");

        return "redirect:/v1/posts";
    }



}
