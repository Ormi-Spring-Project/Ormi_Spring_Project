package com.team8.Spring_Project.application;

import com.team8.Spring_Project.application.dto.PostDto;
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
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
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

        Post post = postDto.toEntity(userRepository, categoryRepository);

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
