package com.team8.Spring_Project.presentation;

import com.team8.Spring_Project.application.PostService;
import com.team8.Spring_Project.application.CommentService;
import com.team8.Spring_Project.domain.Post;
import com.team8.Spring_Project.domain.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/v1/post")
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    @Autowired
    public PostController(PostService postService, CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    @GetMapping("/{id}")
    public String viewPost(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id);
        List<Comment> comments = commentService.getCommentsForPost(id);
        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        return "viewPost";
    }

    // 정적 리소스 요청을 처리하지 않도록 명시적으로 제외
    @GetMapping(value = "/**", produces = "text/html")
    public String handleHtmlRequests() {
        return "forward:/";
    }
}