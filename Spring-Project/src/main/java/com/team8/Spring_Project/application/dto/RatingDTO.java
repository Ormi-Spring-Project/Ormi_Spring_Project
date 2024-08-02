package com.team8.Spring_Project.application.dto;

import com.team8.Spring_Project.domain.Rating;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingDTO {

    private Long id;
    private Integer value;
    private Long commentId;
    private Long userId;
    private Long postId;

    // Entity -> DTO
    public static RatingDTO fromEntity(Rating rating) {
        return RatingDTO.builder()
                .id(rating.getId())
                .value(rating.getValue())
                .commentId(rating.getComment().getId())
                .userId(rating.getUser().getId())
                .postId(rating.getPost().getId())
                .build();
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RatingRequest {
        private Long commentId;
        private Integer value;
        private Long userId;
        private Long postId;
    }
}
