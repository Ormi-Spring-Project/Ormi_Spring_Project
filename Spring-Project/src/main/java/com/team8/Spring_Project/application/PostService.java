package com.team8.Spring_Project.application;

import com.team8.Spring_Project.application.dto.PostDto;
import com.team8.Spring_Project.application.dto.UserService;
import com.team8.Spring_Project.domain.Post;
import com.team8.Spring_Project.infrastructure.persistence.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    private static final Logger log = LoggerFactory.getLogger(PostService.class);

    // PostService는 Post 데이터에만 접근해야한다고 생각해서 postRepository 에 의존성을 갖고, 나머지는 Repository가 아닌 Service에 의존성을 갖도록 구현.
    private final PostRepository postRepository;
    private final UserService userService;
    private final CategoryService categoryService;

    @Autowired
    public PostService(PostRepository postRepository,  UserService userService, CategoryService categoryService) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    // PostList 조회
    @Transactional(readOnly = true)
    public List<PostDto> getAllPosts() {
        return postRepository.findAll().stream()
                .map(PostDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 일반 게시글 상세보기
    @Transactional(readOnly = true)
    public PostDto getPostById(Long id) {
        return postRepository.findById(id)
                .map(PostDto::fromEntity)
                .orElse(null);
    }


    @Transactional
    // 게시글 작성
    public void createPost(PostDto postDto) {

        Post post = postDto.toEntity(userService, categoryService);

        log.info("Creating new id: {}", post.getId());
        log.info("Creating new title: {}", post.getTitle());
        log.info("Creating new content: {}", post.getContent());
        log.info("Creating new application: {}", post.getApplication());
        log.info("Creating new createAt: {}", post.getCreatedAt());
        log.info("Creating new updateAt: {}", post.getUpdatedAt());
        log.info("Creating new userId: {}", post.getUser().getId());
        postRepository.save(post);
    }

    @Transactional
    public void updatePost(Long id, PostDto postDto) {
        Post post = postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("데이터를 찾을 수 없습니다."));
        post.update(
                postDto.getTitle(),
                postDto.getContent(),
                postDto.getApplication()
        );
    }


}
