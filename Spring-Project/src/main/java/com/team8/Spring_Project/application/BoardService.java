package com.team8.Spring_Project.application;

import com.team8.Spring_Project.application.dto.*;
import com.team8.Spring_Project.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

@Service
public class BoardService {

    private final PostService postService;
    private final NoticeService noticeService;

    @Autowired
    public BoardService(PostService postService,
                        NoticeService noticeService) {
        this.postService = postService;
        this.noticeService = noticeService;
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

    // 게시글 삭제
    @Transactional
    public void deleteBoard(Long id, String type) {

        if ("notice".equals(type)) {
            noticeService.deleteNotice(id);
        } else
            postService.deletePost(id);

    }


}
