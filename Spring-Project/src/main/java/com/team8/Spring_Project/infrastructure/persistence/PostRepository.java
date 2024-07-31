package com.team8.Spring_Project.infrastructure.persistence;

import com.team8.Spring_Project.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // 카테고리 ID로 게시글 리스트 조회
    List<Post> findByCategoryId(Long categoryId);


}
