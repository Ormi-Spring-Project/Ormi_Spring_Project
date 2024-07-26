package com.team8.Spring_Project.application.dto;

import com.team8.Spring_Project.domain.Notice;
import com.team8.Spring_Project.infrastructure.persistence.UserRepository;
import lombok.*;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeDto {

    private Long id;

    private String title;

    private String content;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private Long userId;

    // 엔티티를 DTO로 변환하는 정적 메서드
    public static NoticeDto fromEntity(Notice notice) {
        return NoticeDto.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .createdAt(notice.getCreatedAt())
                .updatedAt(notice.getUpdatedAt())
                .userId(notice.getUser().getId())
                .build();
    }

    // DTO를 엔티티로 변환하는 메서드
    public Notice toEntity(UserRepository userRepository) {
        return Notice.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .user(userRepository.findById(this.getUserId()).orElse(null))
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }

}