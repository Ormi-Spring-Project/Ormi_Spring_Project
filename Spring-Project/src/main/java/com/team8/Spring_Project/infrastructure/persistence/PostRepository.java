package com.team8.Spring_Project.infrastructure.persistence;

import com.team8.Spring_Project.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    // 필요한 경우 여기에 추가 쿼리 메서드를 정의할 수 있습니다.
}