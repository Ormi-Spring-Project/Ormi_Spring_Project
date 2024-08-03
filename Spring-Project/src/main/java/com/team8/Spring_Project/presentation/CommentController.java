package com.team8.Spring_Project.presentation;

import com.team8.Spring_Project.application.CommentService;
import com.team8.Spring_Project.application.dto.CommentDTO;
import com.team8.Spring_Project.application.dto.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<List<CommentDTO>> getCommentsByPostId(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getCommentsByPostId(postId));
    }

    @PostMapping
    public ResponseEntity<CommentDTO> createComment(
            @PathVariable Long postId,
            @RequestBody CommentDTO commentDto,
            HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserDTO userDTO = (UserDTO) session.getAttribute("login");

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
            HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserDTO userDTO = (UserDTO) session.getAttribute("login");

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
            HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserDTO userDTO = (UserDTO) session.getAttribute("login");

        if (userDTO == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        commentService.deleteComment(commentId, userDTO.getId());
        return ResponseEntity.noContent().build();
    }
//    평균평점
    @GetMapping("/average-rating")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long postId) {
        double averageRating = commentService.getAverageRatingForPost(postId);
        return ResponseEntity.ok(averageRating);
    }
}