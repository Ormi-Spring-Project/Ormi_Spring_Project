package com.team8.Spring_Project.application.dto;

import lombok.*;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class CommentDTO {
    private Long id;
    private String content;
    private Long userId;
    private String authorNickname; // authorName 대신 authorNickname 사용
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Long postId;

    // 필요한 경우 생성자 추가
    public CommentDTO(Long id, String content, Long userId, String authorNickname, Timestamp createdAt, Timestamp updatedAt, Long postId) {
        this.id = id;
        this.content = content;
        this.userId = userId;
        this.authorNickname = authorNickname;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.postId = postId;
    }
}