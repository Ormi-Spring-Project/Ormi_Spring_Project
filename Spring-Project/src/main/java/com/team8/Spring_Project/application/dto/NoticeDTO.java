package com.team8.Spring_Project.application.dto;

import com.team8.Spring_Project.domain.Notice;
import com.team8.Spring_Project.domain.User;
import lombok.*;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeDTO {

    private Long id;

    private String title;

    private String authorName;

    private String content;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private Long userId;

    // Entity -> DTO
    public static NoticeDTO fromEntity(Notice notice) {

        return NoticeDTO.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .authorName(notice.getUser().getNickname())
                .content(notice.getContent())
                .createdAt(notice.getCreatedAt())
                .updatedAt(notice.getUpdatedAt())
                .userId(notice.getUser().getId())
                .build();

    }

    // DTO -> Entity
    public Notice toEntity(User user) {

        return Notice.builder()
                .title(this.title)
                .content(this.content)
                .user(user)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();

    }

    // NoticeDTO -> BoardDTO
    public static BoardDTO convertNoticeDtoToBoardDto(NoticeDTO noticeDTO, String type) {

        return BoardDTO.builder()
                .id(noticeDTO.getId())
                .title(noticeDTO.getTitle())
                .userId(noticeDTO.getUserId())
                .content(noticeDTO.getContent())
                .createdAt(noticeDTO.getCreatedAt())
                .updatedAt(noticeDTO.getUpdatedAt())
                .authorName(noticeDTO.getAuthorName())
                .type(type)
                .build();

    }

}