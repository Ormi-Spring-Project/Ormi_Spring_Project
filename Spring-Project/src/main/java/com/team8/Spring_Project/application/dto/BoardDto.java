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

    private String authorName;

    private String title;

    private String content;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private String authority;

    private User user;

    // Post에만 해당하는 필드들
    private String application;

    private String categoryName;

    private Long categoryId;
}
