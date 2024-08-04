package com.team8.Spring_Project.application.dto;

import com.team8.Spring_Project.domain.Comment;
import com.team8.Spring_Project.domain.Post;
import com.team8.Spring_Project.domain.User;
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
    private Integer rating;

    // Entity -> DTO
    public static CommentDTO fromComment(Comment comment) {
        return CommentDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .userId(comment.getUser().getId())
                .authorNickname(comment.getUser().getNickname())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .postId(comment.getPost().getId())
                .rating(comment.getRating())
                .build();
    }
    // DTO -> Entity
    public Comment toEntity(User user, Post post) {
        Comment comment = new Comment();
        comment.setId(this.id);
        comment.setContent(this.content);
        comment.setUser(user);
        comment.setPost(post);
        comment.setCreatedAt(this.createdAt);
        comment.setUpdatedAt(this.updatedAt);
        comment.setRating(this.rating);
        return comment;
    }
}