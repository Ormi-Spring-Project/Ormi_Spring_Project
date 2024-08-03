package com.team8.Spring_Project.application.dto;

import lombok.*;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
//생성자 자동생성 추가(전역 사용할때)
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