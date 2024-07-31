package com.team8.Spring_Project.application.dto;

import com.team8.Spring_Project.domain.Category;
import com.team8.Spring_Project.domain.Post;
import com.team8.Spring_Project.domain.User;
import lombok.*;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDTO {

    private Long id;

    private String title;

    private String authorName;

    private String content;

    private String tag;

    private String application;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private Long userId;

    private String categoryName;

    private Long categoryId;

    // Entity -> DTO
    public static PostDTO fromEntity(Post post) {

        return PostDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .authorName(post.getUser().getNickname())
                .content(post.getContent())
                .tag(post.getTag())
                .application(post.getApplication())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .userId(post.getUser().getId())
                .categoryName(post.getCategory().getName())
                .categoryId(post.getCategory().getId())
                .build();

    }

    // DTO -> Entity
    public Post toEntity(User user, Category category) {

        return Post.builder()
                .title(this.title)
                .content(this.content)
                .tag(this.tag)
                .application(this.application)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .user(user)
                .category(category)
                .build();

    }

    // PostDTO -> BoardDTO
    public static BoardDTO convertPostDtoToBoardDto(PostDTO postDTO, String type) {

        return BoardDTO.builder()
                .id(postDTO.getId()) // 상세보기를 위한 BoardDto id 필드 추가에 따른 id 변환
                .title(postDTO.getTitle())
                .userId(postDTO.getUserId())
                .content(postDTO.getContent())
                .application(postDTO.getApplication())
                .createdAt(postDTO.getCreatedAt())
                .updatedAt(postDTO.getUpdatedAt())
                .authorName(postDTO.getAuthorName())
                .categoryId(postDTO.getCategoryId())
                .categoryName(postDTO.getCategoryName())
                .type(type)
                .build();

    }

}