package com.team8.Spring_Project.application;

import com.team8.Spring_Project.application.dto.PostDTO;
import com.team8.Spring_Project.domain.Category;
import com.team8.Spring_Project.domain.Post;
import com.team8.Spring_Project.domain.User;
import com.team8.Spring_Project.infrastructure.persistence.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
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

    // 카테고리 id 기반 일반 게시글 리스트
    @Transactional(readOnly = true)
    public List<PostDTO> getAllPostsByCategory(Long categoryId) {

        return postRepository.findByCategoryId(categoryId).stream()
                .map(PostDTO::fromEntity)
                .collect(Collectors.toList());

    }

    //
    @Transactional(readOnly = true)
    public PostDTO getPostById(Long id) {

        return postRepository.findById(id)
                .map(PostDTO::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("데이터를 찾을 수 없습니다."));

    }

    @Transactional
    // 일반 게시글 작성
    public PostDTO createPost(PostDTO postDto) {

        User user = userService.findUserEntity(postDto.getUserId());

        Category category = categoryService.findCategoryEntity(postDto.getCategoryId());

        Post post = postDto.toEntity(user, category);
        postRepository.save(post);

        return PostDTO.fromEntity(post);
    }

    @Transactional
    // 일반 게시글 수정
    public void updatePost(Long id, PostDTO postDto) {

        Category category = categoryService.findCategoryEntity(postDto.getCategoryId());

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("데이터를 찾을 수 없습니다."));
        post.update(
                postDto.getTitle(),
                postDto.getContent(),
                postDto.getTag(),
                postDto.getApplication(),
                new Timestamp(System.currentTimeMillis()),
                category
        );

    }

    @Transactional
    public void deletePost(Long id) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("데이터를 찾을 수 없습니다."));
        postRepository.delete(post);

    }


}
