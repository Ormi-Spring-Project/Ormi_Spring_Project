package com.team8.Spring_Project.application.dto;

import com.team8.Spring_Project.domain.User;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
/**
 * 이걸 만들지 않으면 BoardService에서 반환타입을 List<Object> 이런 방식으로 해야하고,
 * 클라이언트에서 타입 체크를 해서 사용해야하는데 우리 클라이언트가 Thymeleaf라 어려울 것 같아서 이렇게 구현했음.
  */

public class BoardDto {

    // 상세보기를 위한 BoardDto id 필드 추가.
    private Long id;

    private String authorName;

    private String title;

    private String content;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private String authority;

    private Long userId;

    // Post에만 해당하는 필드들
    private String application;

    private String categoryName;

    private Long categoryId;

    // BoardDto 리스트에서 게시글, 공지사항 구분하기 위한 type
    private String type;

    // BoardDTO SET
    public static BoardDTO createFrom(BoardDTO boardDto, UserDTO userDTO, CategoryDTO categoryDTO) {
        return BoardDTO.builder()
                .title(boardDto.getTitle())
                .authorName(userDTO.getNickname())
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
