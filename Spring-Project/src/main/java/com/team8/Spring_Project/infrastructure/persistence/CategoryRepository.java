package com.team8.Spring_Project.infrastructure.persistence;

import com.team8.Spring_Project.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
