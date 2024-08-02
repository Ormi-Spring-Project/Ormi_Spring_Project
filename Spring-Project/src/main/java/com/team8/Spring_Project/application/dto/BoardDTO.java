package com.team8.Spring_Project.application.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDTO {

    private Long id;

    private String title;

    private byte[] picture;

    private String authorName;

    private String content;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private Long userId;

    // Post에만 해당하는 필드들
    private String application;

    private String categoryName;

    private Long categoryId;

    // BoardDto 리스트에서 게시글, 공지사항 구분하기 위한 type
    private String type;

    // BoardDTO SET
    public static BoardDTO createFrom(BoardDTO boardDto, UserDTO userDTO,
                                      CategoryDTO categoryDTO) {
        return BoardDTO.builder()
                .title(boardDto.getTitle())
                .authorName(userDTO.getNickname())
                .picture(boardDto.getPicture())
                .content(boardDto.getContent())
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .updatedAt(new Timestamp(System.currentTimeMillis()))
                .userId(userDTO.getId())
                .application(boardDto.getApplication())
                .categoryName(categoryDTO.getName())
                .categoryId(categoryDTO.getId())
                .build();
    }

    // boardDTO -> PostDTO
    public static PostDTO convertBoardDtoToPostDto(BoardDTO boardDto) {

        return PostDTO.builder()
                .title(boardDto.getTitle())
                .picture(boardDto.getPicture())
                .content(boardDto.getContent())
                .application(boardDto.getApplication())
                .createdAt(boardDto.getCreatedAt())
                .updatedAt(boardDto.getUpdatedAt())
                .userId(boardDto.getUserId())
                .categoryId(boardDto.getCategoryId())
                .build();

    }

    // boardDTO -> NoticeDTO
    public static NoticeDTO convertBoardDtoToNoticeDto(BoardDTO boardDto) {

        return NoticeDTO.builder()
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
                .userId(boardDto.getUserId())
                .createdAt(boardDto.getCreatedAt())
                .updatedAt(boardDto.getUpdatedAt())
                .build();

    }
}
