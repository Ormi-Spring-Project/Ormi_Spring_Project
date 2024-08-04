package com.team8.Spring_Project.application;

import com.team8.Spring_Project.domain.Comment;
import com.team8.Spring_Project.domain.Post;
import com.team8.Spring_Project.infrastructure.persistence.CommentRepository;
import com.team8.Spring_Project.infrastructure.persistence.PostRepository;
import com.team8.Spring_Project.infrastructure.persistence.RatingRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RatingService {
    private final RatingRepository ratingRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public RatingService(RatingRepository ratingRepository,
                         PostRepository postRepository,
                         CommentRepository commentRepository) {
        this.ratingRepository = ratingRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    public double getAverageRatingForPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        List<Comment> comments = commentRepository.findByPostId(postId);

        if (comments.isEmpty()) {
            return 0.0;
        }

        double sum = comments.stream()
                .mapToInt(Comment::getRating)
                .sum();

        return sum / comments.size();
    }

    public Optional<Integer> getRatingValueForComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
        return Optional.ofNullable(comment.getRating());
    }
}