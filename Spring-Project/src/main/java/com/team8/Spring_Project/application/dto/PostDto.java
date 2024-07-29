package com.team8.Spring_Project.application.dto;

import com.team8.Spring_Project.application.CategoryService;
import com.team8.Spring_Project.domain.Post;
import lombok.*;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDto {

    private Long id;

    private String title;

    private String content;

    private String application;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private String authority;

    private Long userId;

    private Long categoryId;

    // 엔티티를 DTO로 변환하는 정적 메서드
    public static PostDto fromEntity(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .application(post.getApplication())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .authority(post.getUser().getAuthority())
                .userId(post.getUser().getId())
                .categoryId(post.getCategory().getId())
                .build();
    }

    // DTO를 엔티티로 변환하는 메서드
    public Post toEntity(UserService userService,
                         CategoryService categoryService) {
        return Post.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .application(this.application)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .user(userService.getUserById(this.getUserId()))
                .category(categoryService.getCategoryById(this.getCategoryId()))
                .build();
    }

}