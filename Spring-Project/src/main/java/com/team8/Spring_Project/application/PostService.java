package com.team8.Spring_Project.application;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team8.Spring_Project.application.dto.PostDTO;
import com.team8.Spring_Project.domain.*;
import com.team8.Spring_Project.infrastructure.persistence.CommentRepository;
import com.team8.Spring_Project.infrastructure.persistence.PostRepository;
import com.team8.Spring_Project.infrastructure.persistence.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final JPAQueryFactory jpaQueryFactory;
    private final CommentRepository commentRepository; // 모든 게시물 조회

    @Autowired
    public PostService(PostRepository postRepository, UserService userService, CategoryService categoryService, CommentRepository commentRepository,
                      JPAQueryFactory jpaQueryFactory) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.categoryService = categoryService;
        this.commentRepository = commentRepository;
        this.jpaQueryFactory = jpaQueryFactory;
    }

    // 카테고리 id 기반 일반 게시글 리스트
    @Transactional(readOnly = true)
    public List<PostDTO> getAllPostsByCategory(Long categoryId) {

        return postRepository.findByCategoryId(categoryId).stream()
                .map(PostDTO::fromEntity)
                .collect(Collectors.toList());

    }

    // 일반 게시글 조회
    @Transactional(readOnly = true)
    public PostDTO getPostById(Long id) {

        return postRepository.findById(id)
                .map(PostDTO::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("데이터를 찾을 수 없습니다."));

    }

    // 일반 게시글 검색
    @Transactional(readOnly = true)
    public List<PostDTO> searchPostByKeyword(String keyword, String categoryName) {
        QPost post = QPost.post;
        QUser user = QUser.user;

        BooleanBuilder builder = new BooleanBuilder();

        if (keyword != null && !keyword.isEmpty()) {
            builder.or(post.title.containsIgnoreCase(keyword))
                    .or(post.user.nickname.containsIgnoreCase(keyword));
        }

        if (categoryName != null && !categoryName.isEmpty()) {
            builder.and(post.category.name.eq(categoryName));
        }

        List<Post> searchedPosts = jpaQueryFactory.selectFrom(post)
                .leftJoin(post.user, user)
                .where(builder)
                .fetch();

        return searchedPosts.stream()
                .map(PostDTO::fromEntity)
                .collect(Collectors.toList());
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
                postDto.getPicture(),
                postDto.getContent(),
                postDto.getTag(),
                postDto.getApplication(),
                new Timestamp(System.currentTimeMillis()),
                category,
                postDto.getAverageRating() //평균 평점

        );

    }

    @Transactional
    public void deletePost(Long id) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("데이터를 찾을 수 없습니다."));
        postRepository.delete(post);

    }

    @Transactional
    //평균평점 계산
    public void calculateAverageRating(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("데이터를 찾을 수 없습니다."));
    //해당 게시물 모든 댓글 불러오기
        List<Comment> comments = commentRepository.findByPostId(postId);

        if (comments.isEmpty()) {
            post.setAverageRating(0.0);
        } else {
            double averageRating = comments.stream()
                    //각평점을 정수로 변환
                    .mapToInt(Comment::getRating)
                    //평균계산
                    .average()
                    //결과 없으면 0.0
                    .orElse(0.0);
            //평균평점 객체 설정
            post.setAverageRating(averageRating);
        }
        //데이터베이스에 저장
        postRepository.save(post);
    }
}