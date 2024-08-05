package com.team8.Spring_Project.presentation;

import com.team8.Spring_Project.application.CommentService;
import com.team8.Spring_Project.application.PostService;
import com.team8.Spring_Project.application.dto.CommentDTO;
import com.team8.Spring_Project.application.dto.PostDTO;
import com.team8.Spring_Project.application.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import com.team8.Spring_Project.domain.Post;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/v1/posts/{postId}/comments")
public class CommentController {
    private final CommentService commentService;
    private final PostService postService; // PostService 추가

    @Autowired
    public CommentController(CommentService commentService, PostService postService) {
        this.commentService = commentService;
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<CommentDTO>> getCommentsByPostId(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsByPostId(postId));
    }

    @PostMapping
    public ResponseEntity<CommentDTO> createComment(
            @PathVariable Long postId,
            @RequestBody CommentDTO commentDto,
            Authentication authentication) {

        UserDTO userDTO = (UserDTO) authentication.getPrincipal();

        if (userDTO == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        commentDto.setPostId(postId);
        commentDto.setUserId(userDTO.getId());

        CommentDTO createdComment = commentService.createComment(commentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestBody CommentDTO commentDto,
            Authentication authentication) {

        UserDTO userDTO = (UserDTO) authentication.getPrincipal();

        if (userDTO == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        CommentDTO updatedComment = commentService.updateComment(commentId, commentDto, userDTO.getId());
        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            Authentication authentication) {

        UserDTO userDTO = (UserDTO) authentication.getPrincipal();

        if (userDTO == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        commentService.deleteComment(commentId, userDTO.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/average-rating")
    ///http://localhost:8080/v1/posts/post/1?categoryId=1
    //게시글 id=1 일때 /posts/1/average-rating
    //id=1 을 매개 변수로 가져옴
    public ResponseEntity<Double> getAverageRating(@PathVariable Long postId) {
        try {
            PostDTO postDto = postService.getPostById(postId);
            return ResponseEntity.ok(postDto.getAverageRating());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}