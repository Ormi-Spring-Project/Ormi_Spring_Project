package com.team8.Spring_Project.application;

import com.team8.Spring_Project.application.dto.BoardDto;
import com.team8.Spring_Project.application.dto.NoticeDto;
import com.team8.Spring_Project.application.dto.PostDto;
import com.team8.Spring_Project.application.dto.UserService;
import com.team8.Spring_Project.domain.Category;
import com.team8.Spring_Project.domain.Notice;
import com.team8.Spring_Project.domain.Post;
import com.team8.Spring_Project.domain.User;
import com.team8.Spring_Project.presentation.BoardController;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class BoardService {

    // 일단 해결을 위해 Service, Repository가 과도하게 선언되어 있다.
    // Repository -> Service로 변환하여 줄일 수 있도록 리팩토링 해야한다.
    private final PostService postService;
    private final NoticeService noticeService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final CategoryRepository categoryRepository;
    Logger logger = LoggerFactory.getLogger(PostController.class);

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

    // BoardList 조회
    @Transactional
    public List<BoardDto> getAllBoards() {

        // Repository 안쓰고 Service단으로 끝내면 초기화 할 객체가 줄어든다.
        List<BoardDto> noticeList = noticeService.getAllNotices().stream()
                .map(notice -> convertNoticeToBoardDto(notice.toEntity(userService)))
                .toList(); // 여긴 왜 그냥 toList()?

        // Repository 안쓰고 Service단으로 끝내면 초기화 할 객체가 줄어든다.
        List<BoardDto> postList = postService.getAllPosts().stream()
                .map(post -> convertPostToBoardDto(post.toEntity(userService, categoryService)))
                .toList();

        List<BoardDto> boardList = new ArrayList<>();
        boardList.addAll(noticeList);
        boardList.addAll(postList);

        return boardList;
    }

    // 현재는 id기준으로만 상세보기 화면으로 들어가기 때문에 현재는 임의로 설정한 유저 권한이 무엇이냐에 따라 Post / Notice Entity id로 들어간다.
    @Transactional
    public BoardDto getBoardById(Long id) {

        try {
            Post post = postService.getPostById(id).toEntity(userService, categoryService);
            return convertPostToBoardDto(post);
        } catch (EntityNotFoundException e) {
            Notice notice = noticeService.getNoticeById(id).toEntity(userService);
            return convertNoticeToBoardDto(notice);
        }

    }

    @Transactional
    public void createBoard(BoardDto boardDto, String authority) {

        // 테스트 하기 위해서 유저 생성하는 코드
        User testUser = userService.createTestUser();

        Category category = categoryService.getCategoryById(boardDto.getCategoryId());

        boardDto.setUserId(testUser.getId());
        boardDto.setAuthorName(testUser.getNickname());
        boardDto.setCategoryName(category.getName());
        boardDto.setCategoryId(category.getId());
        boardDto.setAuthority(authority);
        boardDto.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        boardDto.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        if ("USER".equals(authority)) {

            PostDto postDto = convertBoardDtoToPostDto(boardDto);
            postService.createPost(postDto);

        } else if ("ADMIN".equals(authority)) {

            NoticeDto noticeDto = convertBoardDtoToNoticeDto(boardDto);
            noticeService.createNotice(noticeDto);

        }

    }


    // 게시글 수정
    @Transactional
    public void updateBoard(Long id, BoardDto boardDto) {

        // 일반 게시글 수정
        PostDto postDto = convertBoardDtoToPostDto(boardDto);
        postService.updatePost(id, postDto);

        // 공지사항 수정
        // NoticeDto noticeDto = convertBoardDtoToNoticeDto(boardDto);
        // noticeService.updateNotice(id,  noticeDto);

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
                .authority(post.getUser().getAuthority())
                .userId(post.getUser().getId())
                .content(post.getContent())
                .application(post.getApplication())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .authorName(post.getUser().getNickname())
                .categoryId(post.getCategory().getId())
                .categoryName(post.getCategory().getName())
                .build();

    }

    // NoticeDto -> BoardDto
    private BoardDto convertNoticeToBoardDto(Notice notice) {

        return BoardDto.builder()
                .id(notice.getId()) // 상세보기를 위한 BoardDto id 필드 추가에 따른 id 변환
                .title(notice.getTitle())
                .userId(notice.getUser().getId())
                .authority(notice.getUser().getAuthority())
                .content(notice.getContent())
                .createdAt(notice.getCreatedAt())
                .updatedAt(notice.getUpdatedAt())
                .authorName(notice.getUser().getNickname())
                .build();

    }

}
