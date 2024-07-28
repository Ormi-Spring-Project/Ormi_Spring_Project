package com.team8.Spring_Project.application;

import com.team8.Spring_Project.application.dto.BoardDto;
import com.team8.Spring_Project.application.dto.NoticeDto;
import com.team8.Spring_Project.application.dto.PostDto;
import com.team8.Spring_Project.application.dto.UserService;
import com.team8.Spring_Project.domain.Category;
import com.team8.Spring_Project.domain.Notice;
import com.team8.Spring_Project.domain.Post;
import com.team8.Spring_Project.domain.User;
import com.team8.Spring_Project.infrastructure.persistence.CategoryRepository;
import com.team8.Spring_Project.infrastructure.persistence.UserRepository;
import com.team8.Spring_Project.presentation.PostController;
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
    private final UserRepository userRepository;
    private final UserService userService;
    private final CategoryRepository categoryRepository;
    Logger logger = LoggerFactory.getLogger(PostController.class);

    @Autowired
    public BoardService(PostService postService,
                        NoticeService noticeService,
                        UserRepository userRepository,
                        UserService userService,
                        CategoryService categoryService,
                        CategoryRepository categoryRepository) {
        this.postService = postService;
        this.noticeService = noticeService;
        this.userRepository = userRepository;
        this.userService = userService;
        this.categoryService = categoryService;
        this.categoryRepository = categoryRepository;
    }

    // BoardList 조회
    @Transactional
    public List<BoardDto> getAllBoards() {

        // Repository 안쓰고 Service단으로 끝내면 초기화 할 객체가 줄어든다.
        List<BoardDto> noticeList = noticeService.getAllNotices().stream()
                .map(notice -> convertNoticeToBoardDto(notice.toEntity(userRepository)))
                .toList(); // 여긴 왜 그냥 toList()?

        // Repository 안쓰고 Service단으로 끝내면 초기화 할 객체가 줄어든다.
        List<BoardDto> postList = postService.getAllPosts().stream()
                .map(post -> convertPostToBoardDto(post.toEntity(userRepository, categoryRepository)))
                .toList();

        List<BoardDto> boardList = new ArrayList<>();
        boardList.addAll(noticeList);
        boardList.addAll(postList);

        return boardList;
    }

    @Transactional
    public void createBoard(BoardDto boardDto, String authority) {

        // 테스트 하기 위해서 유저 생성하는 코드
        User testUser = userService.createTestUser();

        Category category = null;
        if (boardDto.getCategoryId() != null) {
            category = categoryService.getCategoryById(boardDto.getCategoryId());
        }

        boardDto.setUser(testUser);
        boardDto.setAuthorName(testUser.getNickname());
        boardDto.setCategoryName(category.getName());
        boardDto.setCategoryId(category.getId());
        boardDto.setAuthority(authority);
        boardDto.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        boardDto.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        // 데이터 들어오는지 확인을 위한 로그 코드.
        logger.info("Received BoardDto - " +
                        "title: {}, " +
                        "content: {}, " +
                        "application: {}, " +
                        "categoryId: {}, " +
                        "categoryName: {}, " +
                        "authorName: {}, " +
                        "createdAt: {}, " +
                        "updatedAt: {}, " +
                        "authority: {}, " +
                        "userId: {}",
                boardDto.getTitle(),
                boardDto.getContent(),
                boardDto.getApplication(),
                boardDto.getCategoryId(),
                boardDto.getCategoryName(),
                boardDto.getAuthorName(),
                boardDto.getCreatedAt(),
                boardDto.getUpdatedAt(),
                boardDto.getAuthority(),
                boardDto.getUser() != null ? boardDto.getUser().getId() : null);

        if ("USER".equals(authority)) {

            PostDto postDto = convertBoardDtoToPostDto(boardDto);
            postService.createPost(postDto);
            //return convertPostToBoardDto(createdPost.toEntity(userRepository));

        } else if ("ADMIN".equals(authority)) {

            NoticeDto noticeDto = convertBoardDtoToNoticeDto(boardDto);
            noticeService.createNotice(noticeDto);
            //return convertNoticeToBoardDto(createdNotice.toEntity(userRepository));

        }

    }


    // 아래의 메서드들은 Dto로 옮길 필요가 있다?
    // boardDto -> PostDto
    private PostDto convertBoardDtoToPostDto(BoardDto boardDto) {
        return PostDto.builder()
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
                .userId(boardDto.getUser().getId()) // 이게 필요한 건가?
                .application(boardDto.getApplication())
                .createdAt(boardDto.getCreatedAt())
                .updatedAt(boardDto.getUpdatedAt())
                .userId(boardDto.getUser().getId())
                .categoryId(boardDto.getCategoryId())
                .build();
    }

    // boardDto -> NoticeDto
    private NoticeDto convertBoardDtoToNoticeDto(BoardDto boardDto) {

        return NoticeDto.builder()
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
                .userId(boardDto.getUser().getId()) // 이게 필요한 건가?
                .createdAt(boardDto.getCreatedAt())
                .updatedAt(boardDto.getUpdatedAt())
                .build();

    }

    // PostDto -> boardDto
    private BoardDto convertPostToBoardDto(Post post) {

        return BoardDto.builder()
                .title(post.getTitle())
                //.userId(post.getUser().getId()) // 이게 필요한 건가?
                //.authority(post.getUser().getAuthority())
                .content(post.getContent())
                .application(post.getApplication())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .authorName(post.getUser() != null ? post.getUser().getNickname() : "Unknown")
                //.categoryName(post.getCategory().getName())
                .build();

    }

    // NoticeDto -> BoardDto
    private BoardDto convertNoticeToBoardDto(Notice notice) {

        return BoardDto.builder()
                .title(notice.getTitle())
                //.userId(notice.getUser().getId()) // 이게 필요한 건가?
                //.authority(notice.getUser().getAuthority())
                .content(notice.getContent())
                .createdAt(notice.getCreatedAt())
                .updatedAt(notice.getUpdatedAt())
                .authorName(notice.getUser() != null ? notice.getUser().getNickname() : "Unknown")
                .build();

    }

}
