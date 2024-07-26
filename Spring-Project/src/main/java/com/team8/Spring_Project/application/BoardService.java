package com.team8.Spring_Project.application;

import com.team8.Spring_Project.application.dto.BoardDto;
import com.team8.Spring_Project.application.dto.NoticeDto;
import com.team8.Spring_Project.application.dto.PostDto;
import com.team8.Spring_Project.domain.Notice;
import com.team8.Spring_Project.domain.Post;
import com.team8.Spring_Project.infrastructure.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class BoardService {

    private final PostService postService;
    private final NoticeService noticeService;
    private final UserRepository userRepository; // 이거 음..

    @Autowired
    public BoardService(PostService postService, NoticeService noticeService, UserRepository userRepository) {
        this.postService = postService;
        this.noticeService = noticeService;
        this.userRepository = userRepository;
    }

    // BoardList 조회
    @Transactional
    public List<BoardDto> getAllBoards() {

        List<BoardDto> noticeList = noticeService.getAllNotices().stream()
                        .map(notice -> convertNoticeToBoardDto(notice.toEntity(userRepository)))
                        .toList(); // 여긴 왜 그냥 toList()?

        List<BoardDto> postList = postService.getAllPosts().stream()
                .map(post -> convertPostToBoardDto(post.toEntity(userRepository)))
                .toList();

        List<BoardDto> boardList = new ArrayList<>();
        boardList.addAll(noticeList);
        boardList.addAll(postList);

        return boardList;
    }


    // 아래의 메서드들은 Dto로 옮길 필요가 있다?

    // PostDto -> boardDto
    private BoardDto convertPostToBoardDto(Post post) {

        return BoardDto.builder()
                .title(post.getTitle())
                //.userId(post.getUser().getId()) // 이게 필요한 건가?
                //.authority(post.getUser().getAuthority())
                .content(post.getContent())
                .application(post.getApplication())
                .tag(post.getTag())
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
