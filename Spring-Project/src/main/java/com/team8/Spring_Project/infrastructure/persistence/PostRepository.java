package com.team8.Spring_Project.infrastructure.persistence;

import com.team8.Spring_Project.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {


}
