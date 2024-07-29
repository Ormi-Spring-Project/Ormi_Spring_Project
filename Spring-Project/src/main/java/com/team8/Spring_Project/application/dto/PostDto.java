package com.team8.Spring_Project.application.dto;

import com.team8.Spring_Project.domain.Category;
import com.team8.Spring_Project.domain.Post;
import com.team8.Spring_Project.domain.User;
import com.team8.Spring_Project.infrastructure.persistence.CategoryRepository;
import com.team8.Spring_Project.infrastructure.persistence.UserRepository;
import lombok.*;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDto {

    Long id;

    String title;

    String content;

    String application;

    Timestamp createdAt;

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
    public Post toEntity(UserRepository userRepository,
                         CategoryRepository categoryRepository) {
        User user = userRepository.findById(this.getUserId()).orElse(null);
        Category category = categoryRepository.findById(this.getCategoryId()).orElse(null);
        return Post.builder()
                .id(this.id)
                // .id(this.id)
                .title(this.title)
                .content(this.content)
                .application(this.application)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .user(user)
                .category(category)
                .build();
    }

    // DTO를 엔티티로 변환하는 메서드 (파라미터에 service 테스트)
//    public Post toEntity(UserService userService,
//                         CategoryService categoryService) {
//        User user = userService.findUserById(this.getUserId());
//        Category category = categoryService.getCategoryById(this.getCategoryId());
//        return Post.builder()
//                .id(this.id)
//                // .id(this.id)
//                .title(this.title)
//                .content(this.content)
//                .application(this.application)
//                .createdAt(this.createdAt)
//                .updatedAt(this.updatedAt)
//                .user(user)// 일단은 1로, given id null 해결해보기 위함.
//                .category(category)
//                .build();
//    }



}