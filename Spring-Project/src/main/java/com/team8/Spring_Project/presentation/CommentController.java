package com.team8.Spring_Project.presentation;

import com.team8.Spring_Project.application.CommentService;
import com.team8.Spring_Project.domain.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/v1/comment")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/add")
    public String addComment(@RequestParam Long postId, @RequestParam String content, @RequestParam String author) {
        Comment comment = commentService.addComment(postId, content, author);
        // 여기서 comment 객체에 createdAt 정보가 포함되어 있습니다.
        return "redirect:/v1/post/" + postId;
    }

    @PostMapping("/{id}/edit")
    public String editComment(@PathVariable Long id, @RequestParam String content, @RequestParam Long postId) {
        commentService.editComment(id, content);
        return "redirect:/v1/post/" + postId;
    }

    @PostMapping("/{id}/delete")
    public String deleteComment(@PathVariable Long id, @RequestParam Long postId) {
        commentService.deleteComment(id);
        return "redirect:/v1/post/" + postId;
    }
}