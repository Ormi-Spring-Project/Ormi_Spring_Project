package com.team8.Spring_Project.application;

import com.team8.Spring_Project.application.dto.BoardDto;
import com.team8.Spring_Project.application.dto.NoticeDto;
import com.team8.Spring_Project.application.dto.PostDto;
import com.team8.Spring_Project.application.dto.UserDTO;
import com.team8.Spring_Project.domain.*;
import com.team8.Spring_Project.presentation.BoardController;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class BoardService {

    private final PostService postService;
    private final NoticeService noticeService;
    private final CategoryService categoryService;
    private final UserService userService;
    Logger logger = LoggerFactory.getLogger(BoardController.class);

    @Autowired
    public BoardService(PostService postService,
                        NoticeService noticeService,
                        UserService userService,
                        CategoryService categoryService) {
        this.postService = postService;
        this.noticeService = noticeService;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    // 카테고리 id 기반 게시판 리스트
    @Transactional
    public List<BoardDTO> getAllBoards(CategoryDTO categoryDTO) {

        List<BoardDTO> noticeList = noticeService.getAllNotices().stream()
                .map(noticeDTO -> NoticeDTO.convertNoticeDtoToBoardDto(noticeDTO, "notice"))
                .toList();

        List<BoardDTO> postList = postService.getAllPostsByCategory(categoryDTO.getId()).stream()
                .map(postDTO -> PostDTO.convertPostDtoToBoardDto(postDTO, "post"))
                .toList();

        List<BoardDTO> boardList = new ArrayList<>();
        boardList.addAll(noticeList);
        boardList.addAll(postList);

        return boardList;
    }

    // 게시글 상세보기
    @Transactional
    public BoardDTO getBoardById(Long boardId, UserDTO userDTO, String type) throws AccessDeniedException {

        if (!"notice".equals(type) && userDTO.getAuthority() == Authority.BANNED) {
            throw new AccessDeniedException("일반 게시글에 대해 접근 권한이 없습니다.");
        }

        if("notice".equals(type)) {

            // 공지사항 상세보기
            NoticeDTO noticeDTO = noticeService.getNoticeById(boardId);
            return NoticeDTO.convertNoticeDtoToBoardDto(noticeDTO, type);

        } else {

            // 일반 게시글 상세보기
            PostDTO postDTO = postService.getPostById(boardId);
            return PostDTO.convertPostDtoToBoardDto(postDTO, type);

        }

    }

    // 게시글 생성
    @Transactional
    public void createBoard(BoardDTO boardDto, UserDTO userDTO, CategoryDTO categoryDTO) {

        BoardDTO createdBoardDTO = BoardDTO.createFrom(boardDto, userDTO, categoryDTO);

        if (userDTO.getAuthority() == Authority.USER) {

            PostDTO postDto = BoardDTO.convertBoardDtoToPostDto(createdBoardDTO);
            postService.createPost(postDto);

        }

        if (userDTO.getAuthority() == Authority.ADMIN) {

            NoticeDTO noticeDto = BoardDTO.convertBoardDtoToNoticeDto(createdBoardDTO);
            noticeService.createNotice(noticeDto);

        }

    }


    // 게시글 수정
    @Transactional
    public void updateBoard(Long id, BoardDTO boardDto, String type) {

        if("notice".equals(type)) {

            // 공지사항 수정
            NoticeDTO noticeDto = BoardDTO.convertBoardDtoToNoticeDto(boardDto);
            noticeService.updateNotice(id, noticeDto);

        } else {

            // 일반 게시글 수정
            PostDTO postDto = BoardDTO.convertBoardDtoToPostDto(boardDto);
            postService.updatePost(id, postDto);

        }

    }

    @Transactional
    public void deleteBoard(Long id) {

        postService.deletePost(id);

    }

    // 아래의 메서드들은 Dto로 옮길 필요가 있다?
    // boardDto -> PostDto
    private PostDto convertBoardDtoToPostDto(BoardDto boardDto) {
        return PostDto.builder()
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
                .application(boardDto.getApplication())
                .createdAt(boardDto.getCreatedAt())
                .updatedAt(boardDto.getUpdatedAt())
                .userId(boardDto.getUserId())
                .categoryId(boardDto.getCategoryId())
                .build();
    }

    // boardDto -> NoticeDto
    private NoticeDto convertBoardDtoToNoticeDto(BoardDto boardDto) {

        return NoticeDto.builder()
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
                .userId(boardDto.getUserId())
                .createdAt(boardDto.getCreatedAt())
                .updatedAt(boardDto.getUpdatedAt())
                .build();

    }

    // PostDto -> boardDto
    private BoardDto convertPostToBoardDto(Post post) {

        return BoardDto.builder()
                .id(post.getId()) // 상세보기를 위한 BoardDto id 필드 추가에 따른 id 변환
                .title(post.getTitle())
                .userId(post.getUser().getId())
                .content(post.getContent())
                .application(post.getApplication())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .authorName(post.getUser().getNickname())
                .categoryId(post.getCategory().getId())
                .categoryName(post.getCategory().getName())
                .type("Post")
                .build();

    }

    // NoticeDto -> BoardDto
    // NoticeDto에 User에 대한 정보가 없기 때문에 notice를 parameter로 사용.
    private BoardDto convertNoticeToBoardDto(Notice notice) {

        return BoardDto.builder()
                .id(notice.getId()) // 상세보기를 위한 BoardDto id 필드 추가에 따른 id 변환
                .title(notice.getTitle())
                .userId(notice.getUser().getId())
                .content(notice.getContent())
                .createdAt(notice.getCreatedAt())
                .updatedAt(notice.getUpdatedAt())
                .authorName(notice.getUser().getNickname())
                .type("Notice")
                .build();

    }

}
