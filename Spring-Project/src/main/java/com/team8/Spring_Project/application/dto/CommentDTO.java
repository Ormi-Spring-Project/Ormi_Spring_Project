package com.team8.Spring_Project.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDTO {
    private Long id;
    private String content;
    private Long userId;
    private String authorNickname;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Long postId;
    private Integer rating;  // 별점 관련 추가
}