package com.team8.Spring_Project.application;

import com.team8.Spring_Project.application.dto.CommentDTO;
import com.team8.Spring_Project.domain.Comment;
import com.team8.Spring_Project.domain.Post;
import com.team8.Spring_Project.domain.User;
import com.team8.Spring_Project.infrastructure.persistence.CommentRepository;
import com.team8.Spring_Project.infrastructure.persistence.PostRepository;
import com.team8.Spring_Project.infrastructure.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostService postService;

    @Autowired
    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository, PostService postService) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postService = postService;
    }

    @Transactional(readOnly = true)
    public List<CommentDTO> getCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Transactional
    public CommentDTO createComment(CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setContent(commentDTO.getContent());
        comment.setRating(commentDTO.getRating());

        User user = userRepository.findById(commentDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        comment.setUser(user);

        Post post = postRepository.findById(commentDTO.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));
        comment.setPost(post);

        Comment savedComment = commentRepository.save(comment);
        postService.calculateAverageRating(commentDTO.getPostId());
        return convertToDTO(savedComment);
    }

    @Transactional
    public CommentDTO updateComment(Long commentId, CommentDTO commentDTO, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid comment ID"));

        if (!comment.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("User is not authorized to update this comment");
        }

        comment.setContent(commentDTO.getContent());
        comment.setRating(commentDTO.getRating());
        Comment updatedComment = commentRepository.save(comment);
        postService.calculateAverageRating(updatedComment.getPost().getId());
        return convertToDTO(updatedComment);
    }

    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid comment ID"));

        if (!comment.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("User is not authorized to delete this comment");
        }

        Long postId = comment.getPost().getId();
        commentRepository.delete(comment);
        postService.calculateAverageRating(postId);
    }

    private CommentDTO convertToDTO(Comment comment) {
        return CommentDTO.fromComment(comment);
    }
}