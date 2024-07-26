package com.team8.Spring_Project.application.dto;

import com.team8.Spring_Project.domain.Post;
import com.team8.Spring_Project.infrastructure.persistence.UserRepository;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDto {

    Long id;

    String title;

    String content;

    String tag;

    String application;

    Timestamp createdAt;

    Timestamp updatedAt;

    private Long userId;

    private Long categoryId;

    // 엔티티를 DTO로 변환하는 정적 메서드
    public static PostDto fromEntity(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .tag(post.getTag())
                .application(post.getApplication())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .userId(post.getUser().getId())
                .categoryId(post.getCategory().getId())
                .build();
    }

    // DTO를 엔티티로 변환하는 메서드
    public Post toEntity(UserRepository userRepository) {
        return Post.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .user(userRepository.findById(this.getUserId()).orElse(null))// 일단은 1로
                .tag(this.tag)
                .application(this.application)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }

}