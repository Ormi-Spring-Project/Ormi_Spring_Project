package com.team8.Spring_Project.application.dto;

import com.team8.Spring_Project.domain.Category;
import com.team8.Spring_Project.domain.Post;
import com.team8.Spring_Project.domain.User;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

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

    private String application;

    private byte[] picture;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private Long userId;

    private String categoryName;

    private Long categoryId;

    private double averageRating;

    // Entity -> DTO
    public static PostDTO fromEntity(Post post) {

        return PostDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .authorName(post.getUser().getNickname())
                .content(post.getContent())
                .application(post.getApplication())
                .picture(post.getPicture())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .userId(post.getUser().getId())
                .categoryName(post.getCategory().getName())
                .categoryId(post.getCategory().getId())
                //실행시 null값으로 오류 발생 null인경우 기본값 0.0 을 제공하도록 삼항연산자 이용
                .averageRating(post.getAverageRating() != null ? post.getAverageRating().doubleValue() : 0.0) //별점 평균 null이 아니면 값을 반환, null이면 0.0 반환
                .build();

    }

    // DTO -> Entity
    public Post toEntity(User user, Category category) {

        return Post.builder()
                .title(this.title)
                .content(this.content)
                .application(this.application)
                .picture(this.picture)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .user(user)
                .category(category)
                .averageRating(this.averageRating)  // 별점 평균
                .build();

    }

    // PostDTO -> BoardDTO
    public static BoardDTO convertPostDtoToBoardDto(PostDTO postDTO, String type) {

        return BoardDTO.builder()
                .id(postDTO.getId()) // 상세보기를 위한 BoardDto id 필드 추가에 따른 id 변환
                .title(postDTO.getTitle())
                .userId(postDTO.getUserId())
                .picture(postDTO.getPicture())
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