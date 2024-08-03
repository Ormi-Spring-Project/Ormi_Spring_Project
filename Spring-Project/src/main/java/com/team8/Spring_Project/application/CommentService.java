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

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public List<CommentDTO> getCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public CommentDTO createComment(CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setContent(commentDTO.getContent());
        comment.setRating(commentDTO.getRating());  // 별점 설정 추가

        User user = userRepository.findById(commentDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        comment.setUser(user);

        Post post = postRepository.findById(commentDTO.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));
        comment.setPost(post);

        Comment savedComment = commentRepository.save(comment);
        return convertToDTO(savedComment);
    }

    private CommentDTO convertToDTO(Comment comment) {
        return new CommentDTO(
                comment.getId(),
                comment.getContent(),
                comment.getUser().getId(),
                comment.getUser().getNickname(),
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                comment.getPost().getId(),
                comment.getRating()  // rating 필드 추가
        );
    }

    public CommentDTO updateComment(Long commentId, CommentDTO commentDTO, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid comment ID"));

        if (!comment.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("User is not authorized to update this comment");
        }

        comment.setContent(commentDTO.getContent());
        comment.setRating(commentDTO.getRating());  // 별점 업데이트 추가
        Comment updatedComment = commentRepository.save(comment);
        return convertToDTO(updatedComment);
    }

    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid comment ID"));

        if (!comment.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("User is not authorized to delete this comment");
        }

        commentRepository.delete(comment);
    }

    // 평균 별점 계산 메소드 추가
    public double getAverageRatingForPost(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        if (comments.isEmpty()) {
            return 0.0;
        }
        double sum = comments.stream()
                .mapToInt(Comment::getRating)
                .sum();
        return sum / comments.size();
    }
}