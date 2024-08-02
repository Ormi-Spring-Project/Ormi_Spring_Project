package com.team8.Spring_Project.presentation;

import com.team8.Spring_Project.application.RatingService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    private final RatingService ratingService;

    @Autowired
    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<Double> getAverageRatingForPost(@PathVariable Long postId) {
        try {
            double averageRating = ratingService.getAverageRatingForPost(postId);
            return ResponseEntity.ok(averageRating);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/comment/{commentId}")
    public ResponseEntity<Integer> getRatingForComment(@PathVariable Long commentId) {
        try {
            return ratingService.getRatingValueForComment(commentId)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.ok(0)); // 별점이 없는 경우 0을 반환
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}